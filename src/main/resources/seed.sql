MERGE INTO users (id,name,email,password_hash,role) KEY(email)
VALUES (1,'Administrator','admin@tharunikamart.local',
'$2a$10$yYZUQ02ob4xfRVW28F51IuGki0PxrzxV629pfxpuG/JBEUezNMHXS','ADMIN');

MERGE INTO users (id,name,email,password_hash,role) KEY(email)
VALUES (2,'Demo Buyer','buyer@tharunikamart.local',
'$2a$10$yYZUQ02ob4xfRVW28F51IuGki0PxrzxV629pfxpuG/JBEUezNMHXS','BUYER');

MERGE INTO users (id,name,email,password_hash,role) KEY(email)
VALUES (3,'Demo Seller','seller@tharunikamart.local',
'$2a$10$yYZUQ02ob4xfRVW28F51IuGki0PxrzxV629pfxpuG/JBEUezNMHXS','SELLER');
