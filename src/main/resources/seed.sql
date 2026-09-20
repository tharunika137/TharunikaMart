MERGE INTO users (id,name,email,password_hash,role) KEY(email)
VALUES (1,'Administrator','admin@tharunikamart.local',
'$2a$10$6NnTqM4RBphi4LDzmLWeMe9xGbiYLzttVQIggtyuZGIB5dxzTh5VG','ADMIN');

MERGE INTO users (id,name,email,password_hash,role) KEY(email)
VALUES (2,'Demo Buyer','buyer@tharunikamart.local',
'$2a$10$6NnTqM4RBphi4LDzmLWeMe9xGbiYLzttVQIggtyuZGIB5dxzTh5VG','BUYER');

MERGE INTO users (id,name,email,password_hash,role) KEY(email)
VALUES (3,'Demo Seller','seller@tharunikamart.local',
'$2a$10$6NnTqM4RBphi4LDzmLWeMe9xGbiYLzttVQIggtyuZGIB5dxzTh5VG','SELLER');

-- Sample catalog: College Essentials + Fashion, listed by the Demo Seller (id 3).
-- MERGE...KEY(id) keeps this idempotent so restarting the app never creates duplicates.
MERGE INTO products (id,seller_id,name,description,price,stock_qty,category,image_url,listing_type) KEY(id) VALUES
(1,3,'Scientific Calculator','Casio-style fx-991 scientific calculator, ideal for engineering coursework.',799.00,12,'College Essentials','/assets/img/products/scientific-calculator.jpg','SALE'),
(2,3,'Adjustable Study Lamp','Flicker-free LED desk lamp with three brightness levels and a flexible arm.',549.00,15,'College Essentials','/assets/img/products/study-lamp.jpg','SALE'),
(3,3,'Spiral Notebook Set (Pack of 5)','Ruled A4 notebooks, 120 pages each, great for lecture notes.',249.00,30,'College Essentials','/assets/img/products/notebook-set.jpg','SALE'),
(4,3,'Engineering Drawing Kit','Complete geometry box with compass, scales, set squares and protractor.',399.00,20,'College Essentials','/assets/img/products/drawing-kit.jpg','SALE'),
(5,3,'Adjustable Laptop Stand','Foldable aluminium stand that raises your laptop to eye level for better posture.',699.00,10,'College Essentials','/assets/img/products/laptop-stand.jpg','SALE'),
(6,3,'Wireless Mouse','Silent-click wireless mouse with a 1600 DPI sensor and USB receiver.',449.00,25,'College Essentials','/assets/img/products/wireless-mouse.jpg','SALE'),
(7,3,'Water-Resistant Backpack','30L campus backpack with a padded laptop sleeve and multiple pockets.',1099.00,14,'College Essentials','/assets/img/products/backpack.jpg','SALE'),
(8,3,'Whiteboard Marker Set','Pack of 8 low-odour whiteboard markers with an eraser included.',199.00,40,'College Essentials','/assets/img/products/marker-set.jpg','SALE'),
(9,3,'USB-C 4-in-1 Hub','Compact hub with HDMI, USB-A and SD card slots for laptops with limited ports.',899.00,8,'College Essentials','/assets/img/products/usb-hub.jpg','SALE'),
(10,3,'Lab Coat (Formal, White)','Cotton-blend lab coat for chemistry and biology practicals, available for short-term rent.',120.00,6,'College Essentials','/assets/img/products/lab-coat.jpg','RENT'),
(11,3,'Graphing Calculator','Advanced graphing calculator for calculus and statistics courses, rented per day.',60.00,5,'College Essentials','/assets/img/products/graphing-calculator.jpg','RENT'),
(12,3,'Portable Drafting Table','Foldable A2 drafting table for architecture and civil engineering drawings.',150.00,4,'College Essentials','/assets/img/products/drafting-table.jpg','RENT'),
(13,3,'DSLR Camera Kit','Entry-level DSLR with an 18-55mm lens, perfect for project submissions and events.',350.00,3,'College Essentials','/assets/img/products/dslr-camera.jpg','RENT'),
(14,3,'Denim Jacket','Classic unisex denim jacket, mid-wash finish, sizes S to XL.',1299.00,9,'Fashion','/assets/img/products/denim-jacket.jpg','SALE'),
(15,3,'Oversized Graphic Tee','100% cotton oversized T-shirt with a printed graphic front.',449.00,22,'Fashion','/assets/img/products/graphic-tee.jpg','SALE'),
(16,3,'Unisex Sneakers','Lightweight everyday sneakers with a cushioned sole, sizes 6 to 10.',1599.00,11,'Fashion','/assets/img/products/sneakers.jpg','SALE'),
(17,3,'Formal Blazer','Slim-fit formal blazer for interviews and placement drives, available to rent.',249.00,7,'Fashion','/assets/img/products/blazer.jpg','RENT'),
(18,3,'Ethnic Kurta Set','Festive kurta-pyjama set for college fests and functions, available to rent.',299.00,6,'Fashion','/assets/img/products/kurta-set.jpg','RENT'),
(19,3,'Analog Wrist Watch','Minimalist analog watch with a leather strap and stainless steel case.',899.00,13,'Fashion','/assets/img/products/wrist-watch.jpg','SALE'),
(20,3,'Canvas Sling Bag','Compact crossbody sling bag with adjustable strap, great for daily carry.',599.00,18,'Fashion','/assets/img/products/sling-bag.jpg','SALE'),
(21,3,'UV-Protected Sunglasses','Polarized sunglasses with UV400 protection and a lightweight frame.',399.00,20,'Fashion','/assets/img/products/sunglasses.jpg','SALE'),
(22,3,'Fleece Hoodie','Warm fleece-lined hoodie with a kangaroo pocket, unisex fit.',899.00,16,'Fashion','/assets/img/products/hoodie.jpg','SALE'),
(23,3,'Party-Wear Silk Saree','Elegant silk saree with a matching blouse piece, available to rent for events.',399.00,5,'Fashion','/assets/img/products/silk-saree.jpg','RENT'),
(24,3,'Genuine Leather Belt','Reversible leather belt with a rotating buckle, black and brown in one.',349.00,24,'Fashion','/assets/img/products/leather-belt.jpg','SALE'),
(25,3,'Canvas Tote Bag','Sturdy printed canvas tote, ideal for books and everyday essentials.',299.00,28,'Fashion','/assets/img/products/tote-bag.jpg','SALE');