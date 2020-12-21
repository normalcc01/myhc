-- ----------------------------
-- Table structure for product_info
-- ----------------------------
DROP TABLE IF EXISTS product_info;
CREATE TABLE product_info (
                              id int(50) NOT NULL AUTO_INCREMENT ,
                              uuid varchar(50) DEFAULT NULL ,
                              product_name varchar(100) DEFAULT NULL ,
                              supplier_name varchar(100) DEFAULT NULL ,
                              supplier_uuid varchar(50) DEFAULT NULL,
                              type_name varchar(100) DEFAULT NULL ,
                              sale_price double(10,2) DEFAULT NULL ,
                              cost_price double(10,2) DEFAULT NULL ,
                              remark text ,
                              sort int(10) DEFAULT NULL ,
                              yn tinyint(1) DEFAULT '0' ,
                              update_time datetime DEFAULT NULL ,
                              create_time datetime DEFAULT NULL ,
                              PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for supplier_info
-- ----------------------------
DROP TABLE IF EXISTS supplier_info;
CREATE TABLE supplier_info (
                               id int(50) NOT NULL AUTO_INCREMENT ,
                               uuid varchar(50) DEFAULT NULL ,
                               full_name varchar(100) DEFAULT NULL ,
                               remark text ,
                               sort int(10) DEFAULT NULL ,
                               yn tinyint(1) DEFAULT '0' ,
                               update_time datetime DEFAULT NULL ,
                               create_time datetime DEFAULT NULL ,
                               PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for product_bill
-- ----------------------------
DROP TABLE IF EXISTS product_bill;
CREATE TABLE product_bill (
                              id int(50) NOT NULL AUTO_INCREMENT ,
                              uuid varchar(50) DEFAULT NULL ,
                              purchase_order varchar(50) DEFAULT NULL ,
                              purchase_name varchar(100) DEFAULT NULL ,
                              product_name varchar(100) DEFAULT NULL ,
                              product_uuid varchar(50) DEFAULT NULL ,
                              supplier_name varchar(100) DEFAULT NULL ,
                              supplier_uuid varchar(50) DEFAULT NULL ,
#                               typeName varchar(20) DEFAULT NULL ,
                              sale_price double(10,2) DEFAULT NULL ,
                              cost_price double(10,2) DEFAULT NULL ,
                              order_time datetime DEFAULT NULL ,
                              number int(11) DEFAULT NULL ,
                              payment_amount double(10,2) DEFAULT NULL ,
                              arrive tinyint(1) DEFAULT '0' ,
                              arrive_time datetime DEFAULT NULL ,
                              rebates tinyint(1) DEFAULT '0' ,
                              rebates_time datetime DEFAULT NULL ,
                              remark text ,
                              sort int(10) DEFAULT NULL ,
                              yn tinyint(1) DEFAULT '0' ,
                              update_time datetime DEFAULT NULL ,
                              create_time datetime DEFAULT NULL ,
                              PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for purchase_order
-- ----------------------------
DROP TABLE IF EXISTS purchase_order;
CREATE TABLE purchase_order (
                                id int(50) NOT NULL AUTO_INCREMENT ,
                                uuid varchar(50) DEFAULT NULL ,
                                product_uuid varchar(50) DEFAULT NULL ,
                                purchase_name varchar(100) DEFAULT NULL ,
                                order_time datetime DEFAULT NULL ,
                                number int(11) DEFAULT NULL ,
                                price double(10,2) DEFAULT NULL ,
                                payment_amount double(10,2) DEFAULT NULL ,
                                arrive tinyint(1) DEFAULT NULL ,
                                arrive_time datetime DEFAULT NULL ,
                                refund_people varchar(100) DEFAULT NULL ,
                                return_time datetime DEFAULT NULL ,
                                remark text ,
                                sort int(10) DEFAULT NULL ,
                                yn tinyint(1) DEFAULT '0' ,
                                update_time datetime DEFAULT NULL ,
                                create_time datetime DEFAULT NULL ,
                                PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for sales_order
-- ----------------------------
DROP TABLE IF EXISTS sales_order;
CREATE TABLE sales_order (
                             id int(50) NOT NULL AUTO_INCREMENT ,
                             uuid varchar(50) DEFAULT NULL ,
                             product_uuid varchar(50) DEFAULT NULL ,
                             sales_time datetime DEFAULT NULL ,
                             number int(11) DEFAULT NULL ,
                             sale_price double(10,2) DEFAULT NULL ,
                             sale_total_price double(10,2) DEFAULT NULL ,
                             customer_name varchar(100) DEFAULT NULL ,
                             remark text ,
                             sort int(10) DEFAULT NULL ,
                             yn tinyint(1) DEFAULT '0' ,
                             update_time datetime DEFAULT NULL ,
                             create_time datetime DEFAULT NULL ,
                             PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for bill_detail
-- ----------------------------
DROP TABLE IF EXISTS bill_detail;
CREATE TABLE bill_detail (
                                  id int(50) NOT NULL AUTO_INCREMENT ,
                                  uuid varchar(50) DEFAULT NULL ,
                                  order_uuid varchar(50) DEFAULT NULL ,
                                  amount_type varchar(10) DEFAULT NULL ,
                                  amount double(10,2) DEFAULT '0.00' ,

                                  bill_time datetime DEFAULT NULL ,
                                  remark text ,
                                  sort int(10) DEFAULT NULL ,
                                  yn tinyint(1) DEFAULT '0' ,
                                  update_time datetime DEFAULT NULL ,
                                  create_time datetime DEFAULT NULL ,
                                  PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for inventory_register
-- ----------------------------
DROP TABLE IF EXISTS inventory_register;
CREATE TABLE inventory_register (
                                          id int(50) NOT NULL AUTO_INCREMENT ,
                                          uuid varchar(50) DEFAULT NULL ,
                                          order_uuid varchar(50) DEFAULT NULL ,
                                          inventory_uuid varchar(50) DEFAULT NULL ,
                                          inventory_type varchar(10) DEFAULT NULL ,
                                          number int(10) DEFAULT '0' ,
                                          total_price double(10,2) DEFAULT NULL ,
                                          sort int(10) DEFAULT NULL ,
                                          yn tinyint(1) DEFAULT '0' ,
                                          update_time datetime DEFAULT NULL ,
                                          create_time datetime DEFAULT NULL ,
                                          PRIMARY KEY (id)
);

-- ----------------------------
-- Table structure for product_inventory
-- ----------------------------
DROP TABLE IF EXISTS product_inventory;
CREATE TABLE product_inventory (
                                       id int(50) NOT NULL AUTO_INCREMENT ,
                                       uuid varchar(50) DEFAULT NULL ,
                                       product_uuid varchar(50) DEFAULT NULL ,
                                       number int(10) DEFAULT '0' ,
                                       sort int(10) DEFAULT NULL ,
                                       yn tinyint(1) DEFAULT '0' ,
                                       update_time datetime DEFAULT NULL ,
                                       create_time datetime DEFAULT NULL ,
                                       PRIMARY KEY (id)
);
