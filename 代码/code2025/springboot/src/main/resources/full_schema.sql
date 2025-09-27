-- 创建数据库
CREATE DATABASE IF NOT EXISTS code2025;
USE code2025;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    role VARCHAR(20),
    avatar VARCHAR(255),
    state INT DEFAULT 1
);

-- 管理员表
CREATE TABLE IF NOT EXISTS `admin` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    phone VARCHAR(20),
    email VARCHAR(100),
    role VARCHAR(20),
    avatar VARCHAR(255)
);

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL
);

-- 通知表
CREATE TABLE IF NOT EXISTS `notice` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    content TEXT,
    time DATETIME
);

-- 简介表
CREATE TABLE IF NOT EXISTS `introduction` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    img VARCHAR(255),
    title VARCHAR(100) NOT NULL,
    content TEXT,
    time DATETIME,
    category_id INT,
    user_id INT,
    FOREIGN KEY (category_id) REFERENCES category(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 测试结果表
CREATE TABLE IF NOT EXISTS `test_result` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    test_type VARCHAR(50) NOT NULL,
    score INT,
    result_level VARCHAR(50),
    result_description TEXT,
    result_advice TEXT,
    test_date DATETIME,
    answers TEXT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 社区帖子表
CREATE TABLE IF NOT EXISTS community_post (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(20) NOT NULL,
    likes INT DEFAULT 0,
    comments INT DEFAULT 0,
    is_anonymous BOOLEAN DEFAULT FALSE,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    status VARCHAR(10) DEFAULT 'normal',
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 帖子评论表
CREATE TABLE IF NOT EXISTS post_comment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    parent_id INT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    create_time DATETIME NOT NULL,
    status VARCHAR(10) DEFAULT 'normal',
    FOREIGN KEY (post_id) REFERENCES community_post(id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (parent_id) REFERENCES post_comment(id)
);

-- 插入初始管理员账号(可选，密码为123456)
-- INSERT INTO `admin` (username, password, name, role) VALUES ('admin', '123456', '管理员', 'admin');

-- 插入初始分类数据(可选)
-- INSERT INTO `category` (title) VALUES ('技术学习');
-- INSERT INTO `category` (title) VALUES ('职业规划');
-- INSERT INTO `category` (title) VALUES ('学习资源'); 