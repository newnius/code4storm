use book;

create table `book` (
  `asin` varchar(15),
   primary key(`asin`),
  `imurl` varchar(200),
  `title` varchar(200),
  `description` text,
  `price` decimal(8,2)
) ENGINE=innodb DEFAULT CHARSET=utf8;

create table `category`(
  `category_id` int NOT NULL AUTO_INCREMENT,
   primary key(`category_id`),
  `category_name` varchar(100),
   unique(`category_name`),
   index(`category_name`)
)ENGINE=innodb DEFAULT CHARSET=utf8;


create table `book_belong_to_category`(
  `asin` varchar(15) not null,
   foreign key (`asin`) references `book`(`asin`),
   index(`asin`),
  `category_id` int not null,
   foreign key (`category_id`) references `category`(`category_id`),
   index(`category_id`)
)ENGINE=innodb DEFAULT CHARSET=utf8;


create table `list`(
  `list_id` int NOT NULL AUTO_INCREMENT,
   primary key(`list_id`),
  `list_name` varchar(100),
   unique(`list_name`),
   index(`list_name`)
)ENGINE=innodb DEFAULT CHARSET=utf8;


create table `sales_rank`(
  `asin` varchar(15),
   index(`asin`),
   foreign key (`asin`) references `book`(`asin`),
  `list_id` int not null,
   index(`list_id`),
   foreign key (`list_id`) references `list`(`list_id`),
  `rank` int not null
)ENGINE=innodb DEFAULT CHARSET=utf8;


create table `related_books`(
  `asin` varchar(15),
  `related_book_asin` varchar(15),
  `related_type` int
)ENGINE=innodb DEFAULT CHARSET=utf8;


create table `review`(
  `review_id` int NOT NULL AUTO_INCREMENT,
   primary key(`review_id`),
  `reviewer_id` varchar(50),
   index (`reviewer_id`),
  `reviewer` varchar(50),
   index (`reviewer`),
  `asin` varchar(15),
   foreign key (`asin`) references `book` (`asin`),
   index(`asin`),
  `review_text` text,
  `helpful_upvote` int,
  `helpful_sum` int,
  `overall` decimal(2,1),
  `summary`  varchar(200),
  `unix_review_time` int
)ENGINE=innodb DEFAULT CHARSET=utf8;

