DESC tbl_user;

SELECT * FROM tbl_user;

CREATE TABLE ban_ip(
	ip VARCHAR(50) primary key,
	cnt INT DEFAULT 1,
	bandate TIMESTAMP default now()
);

INSERT INTO ban_ip(ip, cnt)
VALUES('192.168.1.12', 5);

DELETE FROM ban_ip;

SELECT * FROM ban_ip;

CREATE TABLE re_tbl_board (
	bno INT PRIMARY KEY auto_increment,
	title VARCHAR(200) NOT NULL,
	content TEXT NOT NULL,
	writer VARCHAR(50),
	origin INT NULL default 0,
	depth INT NULL default 0,
	seq INT NULL default 0,
	regdate TIMESTAMP NULL default now(),
	updatedate TIMESTAMP NULL default now(),
	viewcnt INT NULL default 0,
	showboard VARCHAR(10) NULL default 'y',
	uno INT NOT NULL
);

ALTER TABLE re_tbl_board ADD CONSTRAINT fk_re_tbl_board_uno FOREIGN KEY (uno) REFERENCES tbl_user(uno);

SELECT * FROM re_tbl_board;


CREATE TABLE tbl_attach(
	fullName VARCHAR(200) NOT NULL,
	bno INT NOT NULL,
	regdate TIMESTAMP NULL default now(),
	constraint fk_tbl_attach FOREIGN KEY(bno) REFERENCES re_tbl_board(bno)
);

SELECT * FROM tbl_attach;





