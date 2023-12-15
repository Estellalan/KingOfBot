/*
 Navicat Premium Data Transfer

 Source Server         : 我的数据库
 Source Server Type    : MySQL
 Source Server Version : 80200
 Source Host           : localhost:3306
 Source Schema         : kob

 Target Server Type    : MySQL
 Target Server Version : 80200
 File Encoding         : 65001

 Date: 15/12/2023 18:37:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bot
-- ----------------------------
DROP TABLE IF EXISTS `bot`;
CREATE TABLE `bot`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(15000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createtime` datetime NULL DEFAULT NULL,
  `modifytime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bot
-- ----------------------------
INSERT INTO `bot` VALUES (2, 1, '我的Bot', '自动Bot', 'package com.kob.botrunningsystem.utils;\r\n\r\nimport java.util.ArrayList;\r\nimport java.util.List;\r\n\r\npublic class Bot implements com.kob.botrunningsystem.utils.BotInterface {\r\n    static class Cell {\r\n        public int x, y;\r\n        public Cell(int x, int y) {\r\n            this.x = x;\r\n            this.y = y;\r\n        }\r\n    }\r\n\r\n    private boolean check_tail_increasing(int step) {  // 检验当前回合，蛇的长度是否增加\r\n        if (step <= 10) return true;\r\n        return step % 3 == 1;\r\n    }\r\n\r\n    public List<Cell> getCells(int sx, int sy, String steps) {\r\n        steps = steps.substring(1, steps.length() - 1);\r\n        List<Cell> res = new ArrayList<>();\r\n\r\n        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};\r\n        int x = sx, y = sy;\r\n        int step = 0;\r\n        res.add(new Cell(x, y));\r\n        for (int i = 0; i < steps.length(); i ++ ) {\r\n            int d = steps.charAt(i) - \'0\';\r\n            x += dx[d];\r\n            y += dy[d];\r\n            res.add(new Cell(x, y));\r\n            if (!check_tail_increasing( ++ step)) {\r\n                res.remove(0);\r\n            }\r\n        }\r\n        return res;\r\n    }\r\n\r\n    @Override\r\n    public Integer nextMove(String input) {\r\n        String[] strs = input.split(\"#\");\r\n        int[][] g = new int[13][14];\r\n        for (int i = 0, k = 0; i < 13; i ++ ) {\r\n            for (int j = 0; j < 14; j ++, k ++ ) {\r\n                if (strs[0].charAt(k) == \'1\') {\r\n                    g[i][j] = 1;\r\n                }\r\n            }\r\n        }\r\n\r\n        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);\r\n        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);\r\n\r\n        List<Cell> aCells = getCells(aSx, aSy, strs[3]);\r\n        List<Cell> bCells = getCells(bSx, bSy, strs[6]);\r\n\r\n        for (Cell c: aCells) g[c.x][c.y] = 1;\r\n        for (Cell c: bCells) g[c.x][c.y] = 1;\r\n\r\n        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};\r\n        for (int i = 0; i < 4; i ++ ) {\r\n            int x = aCells.get(aCells.size() - 1).x + dx[i];\r\n            int y = aCells.get(aCells.size() - 1).y + dy[i];\r\n            if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {\r\n                return i;\r\n            }\r\n        }\r\n\r\n        return 0;\r\n    }\r\n}\r\n', '2023-12-15 16:38:37', '2023-12-15 16:56:11');
INSERT INTO `bot` VALUES (3, 2, '初级自动Bot', '自动Bot,但没有加入启发式搜索', 'package com.kob.botrunningsystem.utils;\r\n\r\nimport java.util.ArrayList;\r\nimport java.util.List;\r\n\r\npublic class Bot implements com.kob.botrunningsystem.utils.BotInterface {\r\n    static class Cell {\r\n        public int x, y;\r\n        public Cell(int x, int y) {\r\n            this.x = x;\r\n            this.y = y;\r\n        }\r\n    }\r\n\r\n    private boolean check_tail_increasing(int step) {  // 检验当前回合，蛇的长度是否增加\r\n        if (step <= 10) return true;\r\n        return step % 3 == 1;\r\n    }\r\n\r\n    public List<Cell> getCells(int sx, int sy, String steps) {\r\n        steps = steps.substring(1, steps.length() - 1);\r\n        List<Cell> res = new ArrayList<>();\r\n\r\n        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};\r\n        int x = sx, y = sy;\r\n        int step = 0;\r\n        res.add(new Cell(x, y));\r\n        for (int i = 0; i < steps.length(); i ++ ) {\r\n            int d = steps.charAt(i) - \'0\';\r\n            x += dx[d];\r\n            y += dy[d];\r\n            res.add(new Cell(x, y));\r\n            if (!check_tail_increasing( ++ step)) {\r\n                res.remove(0);\r\n            }\r\n        }\r\n        return res;\r\n    }\r\n\r\n    @Override\r\n    public Integer nextMove(String input) {\r\n        String[] strs = input.split(\"#\");\r\n        int[][] g = new int[13][14];\r\n        for (int i = 0, k = 0; i < 13; i ++ ) {\r\n            for (int j = 0; j < 14; j ++, k ++ ) {\r\n                if (strs[0].charAt(k) == \'1\') {\r\n                    g[i][j] = 1;\r\n                }\r\n            }\r\n        }\r\n\r\n        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);\r\n        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);\r\n\r\n        List<Cell> aCells = getCells(aSx, aSy, strs[3]);\r\n        List<Cell> bCells = getCells(bSx, bSy, strs[6]);\r\n\r\n        for (Cell c: aCells) g[c.x][c.y] = 1;\r\n        for (Cell c: bCells) g[c.x][c.y] = 1;\r\n\r\n        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};\r\n        for (int i = 0; i < 4; i ++ ) {\r\n            int x = aCells.get(aCells.size() - 1).x + dx[i];\r\n            int y = aCells.get(aCells.size() - 1).y + dy[i];\r\n            if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {\r\n                return i;\r\n            }\r\n        }\r\n\r\n        return 0;\r\n    }\r\n}\r\n', '2023-12-15 16:39:26', '2023-12-15 16:55:18');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `a_id` int NULL DEFAULT NULL,
  `a_sx` int NULL DEFAULT NULL,
  `a_sy` int NULL DEFAULT NULL,
  `b_id` int NULL DEFAULT NULL,
  `b_sx` int NULL DEFAULT NULL,
  `b_sy` int NULL DEFAULT NULL,
  `a_steps` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `b_steps` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `map` varchar(15000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `loser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createtime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (1, 1, 11, 1, 2, 1, 12, '', '', '11111111111111100000000000011000010001001110000010000001100000000001111001110100000110000000000001100000101110011110000000000110000001000001110010001000011000000000000111111111111111', 'all', '2023-12-15 16:52:23');
INSERT INTO `record` VALUES (2, 2, 11, 1, 1, 1, 12, '01001', '23332', '11111111111111100000000001011000100000000111000010100011100000000000011101000000000110011000011001100000000010111000000000000111000101000011100000000100011010000000000111111111111111', 'A', '2023-12-15 16:54:19');
INSERT INTO `record` VALUES (3, 2, 11, 1, 1, 1, 12, '01010000011000323032222100', '33333322122222212111100033', '11111111111111110000000000011000000000100110011000110011100000000001011000010010000110000000000001100001001000011010000000000111001100011001100100000000011000000000001111111111111111', 'A', '2023-12-15 16:55:43');
INSERT INTO `record` VALUES (4, 2, 11, 1, 1, 1, 12, '0000000', '2222220', '11111111111111100010000000011000010000000110010100100001110100000001011010000000000110000011000001100000000001011010000000101110000100101001100000001000011000000001000111111111111111', 'all', '2023-12-15 16:56:37');
INSERT INTO `record` VALUES (5, 2, 11, 1, 1, 1, 12, '0100000000011121100', '2303221212222223000', '11111111111111110000101100011000000000000110000001010011100000000010011000001000000110000011000001100000010000011001000000000111001010000001100000000000011000110100001111111111111111', 'A', '2023-12-15 16:56:58');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `rating` int NULL DEFAULT 1500,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'ldh', '$2a$10$ev.Ium1tebmbt8olfx5pUuOF6ZG3kRuXRlsdhnuOKwQeSv9kfrPay', 'https://th.bing.com/th?id=OIP.mCFYR6RNnLiMT521-TSmDQAAAA&w=250&h=250&c=8&rs=1&qlt=90&o=6&dpr=1.5&pid=3.1&rm=2', 1515);
INSERT INTO `user` VALUES (2, 'yxc', '$2a$10$zs0H.HA9lfNEfU8QiMVufOm7LYs7bJeDohK8XXHUa1cQvTIpxKPfW', 'https://th.bing.com/th?id=OIP.RR6081d7yzmAye6o8mNUggAAAA&w=250&h=250&c=8&rs=1&qlt=90&o=6&dpr=1.5&pid=3.1&rm=2', 1494);

SET FOREIGN_KEY_CHECKS = 1;
