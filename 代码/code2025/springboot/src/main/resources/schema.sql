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