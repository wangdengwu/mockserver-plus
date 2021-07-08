package cn.mockserver.plus.interceptor;

import com.google.common.collect.ImmutableSet;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.log.MockServerEventLog;
import org.mockserver.log.model.LogEntry;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.mappers.HttpServletRequestToMockServerHttpRequestDecoder;
import org.mockserver.mock.Expectation;
import org.mockserver.mock.HttpState;
import org.mockserver.mock.action.http.HttpActionHandler;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.MediaType;
import org.mockserver.proxyconfiguration.ProxyConfiguration;
import org.mockserver.responsewriter.ResponseWriter;
import org.mockserver.scheduler.Scheduler;
import org.mockserver.servlet.responsewriter.ServletResponseWriter;
import org.mockserver.socket.tls.NettySslContextFactory;
import org.slf4j.event.Level;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.mockserver.model.HttpResponse.response;

public class MockServerInterceptor implements HandlerInterceptor {

    private MockServerLogger mockServerLogger;
    private HttpState httpStateHandler;
    private Scheduler scheduler;
    private HttpServletRequestToMockServerHttpRequestDecoder httpServletRequestToMockServerRequestDecoder;
    private HttpActionHandler actionHandler;
    private EventLoopGroup workerGroup = new NioEventLoopGroup(ConfigurationProperties.nioEventLoopThreadCount(), new Scheduler.SchedulerThreadFactory(this.getClass().getSimpleName() + "-eventLoop"));

    @SuppressWarnings("WeakerAccess")
    public MockServerInterceptor(String apiServer) {
        this.mockServerLogger = new MockServerLogger(MockServerEventLog.class);
        this.httpServletRequestToMockServerRequestDecoder = new HttpServletRequestToMockServerHttpRequestDecoder(this.mockServerLogger);
        this.scheduler = new Scheduler(mockServerLogger);
        this.httpStateHandler = new HttpState(this.mockServerLogger, this.scheduler);
        this.actionHandler = new HttpActionHandler(workerGroup, httpStateHandler, ProxyConfiguration.proxyConfiguration(ProxyConfiguration.Type.HTTP, apiServer), new NettySslContextFactory(mockServerLogger));
    }

    @PreDestroy
    private void shutdown() {
        mockServerLogger.logEvent(
                new LogEntry()
                        .setLogLevel(Level.INFO)
                        .setMessageFormat("mock server shutdown!")
        );
        this.scheduler.shutdown();
        if (!this.workerGroup.isShuttingDown()) {
            this.workerGroup.shutdownGracefully(100, 750, MILLISECONDS).syncUninterruptibly();
        }
        this.httpStateHandler.stop();
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        service(httpServletRequest, httpServletResponse);
        return false;
    }

    private void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        ResponseWriter responseWriter = new ServletResponseWriter(new MockServerLogger(), httpServletResponse);
        HttpRequest request = null;
        try {
            request = httpServletRequestToMockServerRequestDecoder.mapHttpServletRequestToMockServerRequest(httpServletRequest);

            String portExtension = ":" + httpServletRequest.getLocalPort();
            actionHandler.processAction(request, responseWriter, null, ImmutableSet.of(
                    httpServletRequest.getLocalAddr() + portExtension,
                    "localhost" + portExtension,
                    "127.0.0.1" + portExtension
            ), true, true);
        } catch (IllegalArgumentException iae) {
            mockServerLogger.logEvent(
                    new LogEntry()
                            .setLogLevel(Level.ERROR)
                            .setHttpRequest(request)
                            .setMessageFormat("exception processing request:{}error:{}")
                            .setArguments(request, iae.getMessage())
            );
            responseWriter.writeResponse(request, BAD_REQUEST, iae.getMessage(), MediaType.create("text", "plain").toString());
        } catch (Exception e) {
            mockServerLogger.logEvent(
                    new LogEntry()
                            .setLogLevel(Level.ERROR)
                            .setHttpRequest(request)
                            .setMessageFormat("exception processing " + request)
                            .setThrowable(e)
            );
            responseWriter.writeResponse(request, response().withStatusCode(BAD_REQUEST.code()).withBody(e.getMessage()), true);
        }
    }

    public List<Expectation> add(Expectation... expectations) {
        return httpStateHandler.add(expectations);
    }

    public void delete(HttpRequest httpRequest) {
        httpStateHandler.clear(httpRequest);
    }
}
