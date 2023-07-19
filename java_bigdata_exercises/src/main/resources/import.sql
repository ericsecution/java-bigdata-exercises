DROP TABLE IF EXISTS registrations CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;

CREATE TABLE students (
	id SERIAL PRIMARY KEY,
	full_name VARCHAR(100),
	age INTEGER,
	email VARCHAR(100),
	course VARCHAR(100)
);

CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    instructor VARCHAR(100)
);

CREATE TABLE registrations (
    id SERIAL PRIMARY KEY,
    student_id INTEGER REFERENCES students(id),
    course_id INTEGER REFERENCES courses(id)
);

-- Inserting 'Default' Students
INSERT INTO students (full_name, age, email, course) VALUES
('Elon Musk', 50, 'rocketman@first2marzman.com', 'Hibernate: Unleashing the Power of Java Persistence'),
('Ada Lovelace', 36, 'theog@analyticalmindprogramming.com', 'Spring MVC: Springing into Web Development'),
('Alan Turing', 41, 'enigma@wrappeninacodebreaker.com', 'React: Building UIs at Light Speed'),
('Grace Hopper', 45, 'cobolqueen@navy.mil', 'YOLO: Drink Your v8'),
('Linus Torvalds', 51, 'penguinsarepeopletoo@linuxnationplaceworld.com', 'LLMs: Unleashing the Power of Low-Level Programming'),
('Guido van Rossum', 65, 'montypython@snakecharmer.com', 'Big Data: Handling Data at Scale'),
('Ryan Reynolds', 46, 'coldbruv@javajungle.com', 'Java: Brewing Up Some Magic'),
('Bjarne Stroustrup', 37, 'cpp@languagecraftrus.com', 'JavaScript: Leveraging the Language of the Web'),
('Rasmus Lerdorf', 52, 'web@phpisitstillcool.com', 'Good MERN''n, Time to Start PERN''n'),
('Taylor Swift', 33, 'node@eventlooptimestwo.com', 'CSS Frameworks & Libraries: Tailwind, Materialize, Animista'),
('Jordan Walke', 35, 'reactive@facemanualbooktown.com', 'Hadoop: Taming the Elephant'),
('Dennis Ritchie', 70, 'lightmeup@friendofc.com', 'Spark: Igniting the Power of Big Data'),
('Ken Thompson', 37, 'shellgame@bellunixlabstations.com', 'Hive: The Honey of Data Warehousing'),
('John Backus', 42, 'ifonlythiswerearound@dontforgetaboutibm.com', 'PostgreSQL: The Elephant in the Room'),
('Donald Knuth', 23, 'artofprogramming@stanfordnerdfactory.edu', 'Express.js: Expressing the Power of JavaScript'),
('Roscoe C. Giles, III', 45, 'usaphysicist@bostonuniv.edu', 'Node.js: JavaScript in Every Corner of the Galaxy'),
('Yann LeCun', 61, 'deeplearning@nyuatnyc.edu', 'TypeScript: Typing Your Way to JavaScript Supremacy'),
('Geoffrey Hinton', 73, 'backpropagation@toronto.edu', 'Blockchain: The Chain of Trust'),
('Yoshua Bengio', 57, 'ai4web3@umontreal.ca', 'CosmWasm: Sailing the Seas of Smart Contracts'),
('Anonymous Nakamoto', 40, 'bitcoin@blockchainorsatsgame.com', 'NFT Creation: The Art of Digital Ownership'),
('Bob Patrick', 28, 'mrcrab@layer2islevel3.com', 'Rust: Building Reliable and Efficient Software'),
('Andy Sterkowitz', 30, 'yallknowme@jsfromolmozilla.com', 'YouTube API: Tapping in to the "How To"'),
('Douglas Crockford', 65, 'jsonnotjason@2testornot2test.com', 'Cypress: Testing Your Way to Success'),
('Michael Widenius', 59, 'mysqlateursql@mariadb.com', 'Swagger: Documenting Your API Like a Pro'),
('Robert M. Bell', 48, 'statking@attlabs.com', 'Postman: Delivering API Requests'),
('Annie Easley', 38, 'rainforest@adiosbookstores.com', 'Jest: Jesting Your Way to Testing Success'),
('Melba Ray Mouton', 31, 'typescriptress@wontharmurxbox.com', 'Google Cloud: Soaring in the Clouds'),
('Eichiiro Oda', 47, 'onepiece@manga1son.com', 'AWS Cloud: Scaling the Heights of Cloud Computing'),
('Becky Miyazaki', 21, 'ggdaughter@ghibligongett.com', 'React: Building UIs at Light Speed'),
('John Resig', 36, 'jquerier@vsvanillajavascript.com', 'YOLO: Look, You Only Learn Once');

-- Inserting 'Default' Courses and Instructors
INSERT INTO courses (name, instructor) VALUES
('Hibernate: Unleashing the Power of Java Persistence', 'The Esteemed Mr. Dr. Gavin King, III, Esq. Hibernate'),
('Spring MVC: Springing into Web Development', 'Rod "Spring King" Johnson'),
('React: Data Visualization', 'Blair "The Bear" Vanderhoof, First of His Name'),
('YOLO: Drink Your v8', 'Chad "The Chad" Chad, III'),
('LLMs: Unleashing the Power of Low-Level Programming', 'Mr. "It''s Back Here!" Chris Lattner, LLVM'),
('Big Data: Handling Data at Scale', 'Mr. 538, The Great Nate Silver'),
('Java: Brewing Up Some Magic', 'James "Java Jungle" Gosling'),
('JavaScript: Leveraging the Language of the Web', 'Brendan "JavaScript Jedi" Eich'),
('Good MERN''n, Time to Start PERN''n', 'Tim Berners-Lee, Certified Hero'),
('CSS Frameworks & Libraries: Tailwind, Materialize, Animista', 'Håkon "The Conqueror" Wium Lie'),
('Hadoop: Taming the Elephant', 'Doug "So Sharp, I''m" Cutting'),
('Spark: Igniting the Power of Big Data', 'Matei "Light ''em Up!" Zaharia'),
('Hive: The Honey of Data Warehousing', 'Ashish "is a Home" Thusoo (pronounced while holding your tongue)'),
('PostgreSQL: The Elephant in the Room', '"Mithril" Michael Stonebraker, Moria Mine Lineage'),
('Express.js: Expressing the Power of JavaScript', 'TJ "Express.js" Holowaychuk'),
('Node.js: JavaScript in Every Corner of the Galaxy', 'Ryan "No Relation to Roald" Dahl'),
('TypeScript: Typing Your Way to JavaScript Supremacy', 'Anders "TypeScript" Hejlsberg'),
('Blockchain: The Chain of Trust', 'Satoshi "I''m Not Shakespeare!" Nakamoto'),
('CosmWasm: Sailing the Seas of Smart Contracts', 'Ethan "Join The" Frey'),
('NFT Creation: The Art of Digital Ownership', 'Vitalik "Big-Brain Crown Posse" Buterin'),
('Rust: Building Reliable and Efficient Software', 'Sir Graydon Hoare of the 7 Seas'),
('YouTube API: Tapping in to the "How To"', 'Chad "Not The Clothing Company" Hurley'),
('Cypress: Testing Your Way to Success', 'Brian "Cypress Hill" Mann'),
('Swagger: Documenting Your API Like a Pro', 'Tony "Swaggy T" Tam'),
('Postman: Delivering API Requests', 'Abhinav Asthana aka "The Myntra Marvel"'),
('Jest: Jesting Your Way to Testing Success', 'Christoph Nakazawa, The Jasmine Fork'),
('Google Cloud: Soaring in the Clouds', 'Urs (is now "Mine") Hölzle'),
('AWS Cloud: Scaling the Heights of Cloud Computing', 'Andy "Always Keepin'' it Classy" Jassy'),
('React: Building UIs at Light Speed', 'Sebastian Markbage, Engineering Dir., Facebook--wait!--I mean: Meta'),
('YOLO: Look, You Only Learn Once', 'Joseph (Method Mon and) Redmon');


-- Inserting 'Default' Registrations
INSERT INTO registrations (student_id, course_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10),
(11, 11),
(12, 12),
(13, 13),
(14, 14),
(15, 15),
(16, 16),
(17, 17),
(18, 18),
(19, 19),
(20, 20),
(21, 21),
(22, 22),
(23, 23),
(24, 24),
(25, 25),
(26, 26),
(27, 27),
(28, 28),
(29, 29),
(30, 30);
