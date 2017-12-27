
CREATE TABLE IF NOT EXISTS `org_table` (
	`org_id` int(5) NOT NULL AUTO_INCREMENT,
	`org_name` TINYTEXT NOT NULL,
	`contact_name` TINYTEXT NOT NULL,
	`contact_number` TINYTEXT NOT NULL,
	`contact_email` TINYTEXT NOT NULL,
	`notes` MEDIUMTEXT NOT NULL,
	PRIMARY KEY (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS user_table (
	user_id int(5) NOT NULL AUTO_INCREMENT, 
	username TINYTEXT NOT NULL, 
	password TINYTEXT NOT NULL, 
	user_email TINYTEXT NOT NULL,
	PRIMARY KEY (user_id) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `donation_table` (
	`order_id` int(5) NOT NULL AUTO_INCREMENT,
	`org_id` int(5) NOT NULL,
	`org_name` TINYTEXT NOT NULL,
	`category` TINYTEXT NOT NULL,
	`weight` int(7) NOT NULL,
	`donation` int(1) NOT NULL,
	`user_name` TINYTEXT NOT NULL,
	`date` DATE NOT NULL,
	`ts` TIMESTAMP,
	PRIMARY KEY (`order_id`),
	FOREIGN KEY (`org_id`) REFERENCES org_table(org_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `redirection_table` (
  `user_name` TINYTEXT NOT NULL,
  `page` TINYTEXT NOT NULL,
  `ts` TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `reports_table` (
  `user_name` TINYTEXT NOT NULL,
  `tr_tab` TINYTEXT NOT NULL,
  `io_tab` TINYTEXT NOT NULL,
  `sd_tab` TINYTEXT NOT NULL,
  `ts` TIMESTAMP
)ENGINE=InnoDB DEFAULT CHARSET=latin1;