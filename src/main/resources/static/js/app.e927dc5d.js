(function(e){function t(t){for(var n,a,u=t[0],i=t[1],l=t[2],s=0,f=[];s<u.length;s++)a=u[s],Object.prototype.hasOwnProperty.call(o,a)&&o[a]&&f.push(o[a][0]),o[a]=0;for(n in i)Object.prototype.hasOwnProperty.call(i,n)&&(e[n]=i[n]);p&&p(t);while(f.length)f.shift()();return c.push.apply(c,l||[]),r()}function r(){for(var e,t=0;t<c.length;t++){for(var r=c[t],n=!0,a=1;a<r.length;a++){var i=r[a];0!==o[i]&&(n=!1)}n&&(c.splice(t--,1),e=u(u.s=r[0]))}return e}var n={},o={app:0},c=[];function a(e){return u.p+"js/"+({about:"about"}[e]||e)+"."+{about:"601e95cd"}[e]+".js"}function u(t){if(n[t])return n[t].exports;var r=n[t]={i:t,l:!1,exports:{}};return e[t].call(r.exports,r,r.exports,u),r.l=!0,r.exports}u.e=function(e){var t=[],r=o[e];if(0!==r)if(r)t.push(r[2]);else{var n=new Promise((function(t,n){r=o[e]=[t,n]}));t.push(r[2]=n);var c,i=document.createElement("script");i.charset="utf-8",i.timeout=120,u.nc&&i.setAttribute("nonce",u.nc),i.src=a(e);var l=new Error;c=function(t){i.onerror=i.onload=null,clearTimeout(s);var r=o[e];if(0!==r){if(r){var n=t&&("load"===t.type?"missing":t.type),c=t&&t.target&&t.target.src;l.message="Loading chunk "+e+" failed.\n("+n+": "+c+")",l.name="ChunkLoadError",l.type=n,l.request=c,r[1](l)}o[e]=void 0}};var s=setTimeout((function(){c({type:"timeout",target:i})}),12e4);i.onerror=i.onload=c,document.head.appendChild(i)}return Promise.all(t)},u.m=e,u.c=n,u.d=function(e,t,r){u.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:r})},u.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},u.t=function(e,t){if(1&t&&(e=u(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var r=Object.create(null);if(u.r(r),Object.defineProperty(r,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var n in e)u.d(r,n,function(t){return e[t]}.bind(null,n));return r},u.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return u.d(t,"a",t),t},u.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},u.p="/",u.oe=function(e){throw console.error(e),e};var i=window["webpackJsonp"]=window["webpackJsonp"]||[],l=i.push.bind(i);i.push=t,i=i.slice();for(var s=0;s<i.length;s++)t(i[s]);var p=l;c.push([0,"chunk-vendors"]),r()})({0:function(e,t,r){e.exports=r("56d7")},"3e13":function(e,t,r){},"56d7":function(e,t,r){"use strict";r.r(t);r("e260"),r("e6cf"),r("cca6"),r("a79d"),r("d3b7");var n=r("bc3a"),o=r.n(n),c={baseURL:(Object({NODE_ENV:"production",BASE_URL:"/"}).VUE_APP_BASEURL||"")+"/_mock_server",timeout:3e4,withCredentials:!0},a=o.a.create(c);a.interceptors.request.use((function(e){return e}),(function(e){return Promise.reject(e)})),a.interceptors.response.use((function(e){return e}),(function(e){return Promise.reject(e)}));var u=a,i=r("7a23"),l={id:"nav"},s=Object(i["createTextVNode"])("Home"),p=Object(i["createTextVNode"])(" | "),f=Object(i["createTextVNode"])("About");function d(e,t){var r=Object(i["resolveComponent"])("router-link"),n=Object(i["resolveComponent"])("router-view");return Object(i["openBlock"])(),Object(i["createBlock"])("div",l,[Object(i["createVNode"])(r,{to:"/"},{default:Object(i["withCtx"])((function(){return[s]})),_:1}),p,Object(i["createVNode"])(r,{to:"/about"},{default:Object(i["withCtx"])((function(){return[f]})),_:1}),Object(i["createVNode"])(n)])}r("dfca");const b={};b.render=d;var v=b,m=(r("3ca3"),r("ddb0"),r("6c02")),j=r("cf05"),O=r.n(j),h={class:"home"},g=Object(i["createVNode"])("img",{alt:"Vue logo",src:O.a},null,-1);function y(e,t,r,n,o,c){var a=Object(i["resolveComponent"])("HelloWorld");return Object(i["openBlock"])(),Object(i["createBlock"])("div",h,[g,Object(i["createVNode"])(a,{msg:n.msg},null,8,["msg"])])}var w=Object(i["withScopeId"])("data-v-42a311bf");Object(i["pushScopeId"])("data-v-42a311bf");var _={class:"hello"};Object(i["popScopeId"])();var k=w((function(e,t,r,n,o,c){return Object(i["openBlock"])(),Object(i["createBlock"])("div",_,[Object(i["createVNode"])("h1",null,Object(i["toDisplayString"])(r.msg),1)])})),P={name:"HelloWorld",props:{msg:String}};P.render=k,P.__scopeId="data-v-42a311bf";var V=P;function x(){return u.get("/test")}var S={name:"Home",components:{HelloWorld:V},setup:function(){var e=Object(i["ref"])("Welcome to Your Vue.js App");return x().then((function(t){e.value=t.data})),{msg:e}}};S.render=y;var N=S,A=[{path:"/",name:"Home",component:N},{path:"/about",name:"About",component:function(){return r.e("about").then(r.bind(null,"f820"))}}],B=Object(m["a"])({history:Object(m["b"])("/"),routes:A}),C=B,E=(r("4af4"),r("f3fc"),r("44fb")),T=r.n(E),H=r("3ef0"),L=r.n(H),I=r("51d4"),M=r.n(I),U=function(e){M.a.use(L.a),e.use(T.a)},W=Object(i["createApp"])(v);U(W),W.use(C).mount("#app")},cf05:function(e,t,r){e.exports=r.p+"img/logo.9847527d.png"},dfca:function(e,t,r){"use strict";r("3e13")}});
//# sourceMappingURL=app.e927dc5d.js.map