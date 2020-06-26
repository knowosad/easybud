
INSERT INTO t_contractors VALUES
(2,	'Contractor1', 'Contractor1', 'Contractor1', '123456789', 'contractor1', 'ACTIVE'),
(3, 'Contractor2', 'Contractor2', 'Contractor2', '123456789', 'contractor2', 'ACTIVE'),
(4,	'Contractor3', 'Contractor3', 'Contractor3', '123456789', 'contractor3', 'ACTIVE');

INSERT INTO t_orders VALUES
(1,	'Order1',	'2020-07-31',	'Order1',	'Order1',	'order1',	'2020-06-01',	'ACTIVE',	2),
(5,	'Order2',	'2020-06-30',	'Order2',	'Order2',	'order2',	'2020-06-01',	'ACTIVE',	3),
(6,	'Order3',	'2020-06-20',	'Order3',	'Order3',	'order3',	'2020-06-07',	'ACTIVE',	4);

INSERT INTO t_employees VALUES
(7,	    'Employee1',	'Employee1',	'Employee1',	'employee1',	'ACTIVE',	1),
(8,	    'Employee2',	'Employee2',	'Employee2',	'employee2',	'ACTIVE',	1),
(9,	    'Employee3',	'Employee3',	'Employee3',	'employee3',	'ACTIVE',	5),
(10,	'Employee4',	'Employee4',	'Employee4',	'employee4',	'ACTIVE',	5),
(11,	'Employee5',	'Employee5',	'Employee5',	'employee5',	'ACTIVE',	6),
(12,	'Employee6',	'Employee6',	'Employee6',	'employee6',	'ACTIVE',	6);

INSERT INTO t_notifications VALUES
(13,	'2020-06-26',	'Notification1',	'NOTE',	    'admin',	1),
(14,	'2020-06-26',	'Notification1',	'ORDER',	'admin',	1),
(15,	'2020-06-26',	'Notification1',	'ACCIDENT',	'admin',	1),
(16,	'2020-06-26',	'Notification1',	'NOTE',	    'admin',	5),
(17,	'2020-06-26',	'Notification1',	'ORDER',	'admin',	5),
(18,	'2020-06-26',	'Notification1',	'ACCIDENT',	'admin',	5),
(19,	'2020-06-26',	'Notification1',	'NOTE',	    'admin',	6),
(20,	'2020-06-26',	'Notification1',	'ORDER',	'admin',	6),
(21,	'2020-06-26',	'Notification1',	'ACCIDENT',	'admin',	6);