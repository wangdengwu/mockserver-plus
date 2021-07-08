ALTER TABLE `mockserver`.`api_expectation`
MODIFY COLUMN `request_body_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `request_body`;