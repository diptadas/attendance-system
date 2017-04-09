-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Nov 21, 2016 at 03:10 PM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `attendance_system_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE IF NOT EXISTS `attendance` (
  `assignment_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `date_time` datetime NOT NULL,
  `is_present` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`assignment_id`, `student_id`, `date_time`, `is_present`) VALUES
(1, 1104029, '2016-11-20 23:19:27', 0),
(1, 1104029, '2016-11-21 19:31:29', 1),
(1, 1104033, '2016-11-20 23:19:27', 1),
(1, 1104033, '2016-11-21 19:31:29', 1),
(1, 1104037, '2016-11-20 23:19:27', 1),
(1, 1104037, '2016-11-21 19:31:29', 0),
(1, 1104042, '2016-11-20 23:19:27', 0),
(1, 1104042, '2016-11-21 19:31:29', 1),
(1, 1104045, '2016-11-20 23:19:27', 0),
(1, 1104045, '2016-11-21 19:31:29', 0),
(1, 1104047, '2016-11-20 23:19:27', 1),
(1, 1104047, '2016-11-21 19:31:29', 1),
(1, 1104048, '2016-11-20 23:19:27', 1),
(1, 1104048, '2016-11-21 19:31:29', 1),
(1, 1104049, '2016-11-20 23:19:27', 1),
(1, 1104049, '2016-11-21 19:31:29', 1),
(1, 1104051, '2016-11-20 23:19:27', 1),
(1, 1104051, '2016-11-21 19:31:29', 0),
(1, 1104060, '2016-11-20 23:19:27', 0),
(1, 1104060, '2016-11-21 19:31:29', 1);

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE IF NOT EXISTS `course` (
  `course_id` varchar(100) NOT NULL,
  `course_title` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`course_id`, `course_title`) VALUES
('CSE-419', 'VLSI Design'),
('CSE-423', 'Digital Systems Design'),
('CSE-431', 'Compiler Design'),
('CSE-435', 'Digital Image Processing'),
('CSE-453', 'Information Security and Control'),
('CSE-457', 'Computer Graphics');

-- --------------------------------------------------------

--
-- Table structure for table `course_assignment`
--

CREATE TABLE IF NOT EXISTS `course_assignment` (
`assignment_id` int(11) NOT NULL,
  `course_id` varchar(11) NOT NULL,
  `teacher_user_id` int(11) NOT NULL,
  `session` int(11) NOT NULL,
  `section` varchar(100) NOT NULL,
  `is_running` tinyint(1) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `course_assignment`
--

INSERT INTO `course_assignment` (`assignment_id`, `course_id`, `teacher_user_id`, `session`, `section`, `is_running`) VALUES
(1, 'CSE-423', 8, 2016, 'A', 1),
(2, 'CSE-457', 6, 2016, 'A', 1),
(3, 'CSE-419', 9, 2016, 'A', 1),
(4, 'CSE-453', 10, 2016, 'A', 1),
(5, 'CSE-435', 7, 2016, 'A', 1),
(6, 'CSE-419', 8, 2015, 'A', 0);

-- --------------------------------------------------------

--
-- Table structure for table `course_registration`
--

CREATE TABLE IF NOT EXISTS `course_registration` (
  `assignment_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `course_registration`
--

INSERT INTO `course_registration` (`assignment_id`, `student_id`) VALUES
(1, 1104029),
(2, 1104029),
(3, 1104029),
(4, 1104029),
(1, 1104033),
(2, 1104033),
(3, 1104033),
(5, 1104033),
(1, 1104037),
(2, 1104037),
(3, 1104037),
(4, 1104037),
(1, 1104042),
(2, 1104042),
(3, 1104042),
(5, 1104042),
(1, 1104045),
(2, 1104045),
(3, 1104045),
(5, 1104045),
(1, 1104047),
(2, 1104047),
(3, 1104047),
(5, 1104047),
(1, 1104048),
(2, 1104048),
(3, 1104048),
(5, 1104048),
(1, 1104049),
(2, 1104049),
(3, 1104049),
(4, 1104049),
(1, 1104051),
(2, 1104051),
(3, 1104051),
(4, 1104051),
(1, 1104060),
(2, 1104060),
(3, 1104060),
(4, 1104060);

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE IF NOT EXISTS `department` (
  `dept_id` int(11) NOT NULL,
  `dept_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`dept_id`, `dept_name`) VALUES
(1, 'CSE'),
(2, 'EEE'),
(3, 'ME'),
(4, 'CE');

-- --------------------------------------------------------

--
-- Table structure for table `user_info`
--

CREATE TABLE IF NOT EXISTS `user_info` (
`user_id` int(11) NOT NULL,
  `user_name` varchar(100) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `user_type` int(11) NOT NULL,
  `is_admin` tinyint(1) NOT NULL,
  `dept_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `section` varchar(100) NOT NULL,
  `designation` varchar(100) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

--
-- Dumping data for table `user_info`
--

INSERT INTO `user_info` (`user_id`, `user_name`, `full_name`, `email`, `password`, `user_type`, `is_admin`, `dept_id`, `student_id`, `section`, `designation`) VALUES
(1, 'dipta', 'Dipta Das', 'abc@gmail.com', 'dipta', 1, 0, 1, 1104051, 'A', ''),
(2, 'arif', 'Ariful Islam', 'abc@gmail.com', 'arif', 1, 0, 1, 1104048, 'A', ''),
(3, 'tousif', 'Tousifur Rahman Khan', 'abc@gmail.com', 'tousif', 1, 0, 1, 1104037, 'A', ''),
(4, 'kamal', 'Mahiuddin Al Kamal', 'abc@gmail.com', 'kamal', 1, 0, 1, 1104047, 'A', ''),
(5, 'moshiul', 'Dr. Mohammed Moshiul Hoque', 'abc@gmail.com', 'moshiul', 2, 1, 1, 0, '', 'Head'),
(6, 'ibrahim', 'Dr. Md. Ibrahim Khan', 'abc@gmail.com', 'ibrahim', 2, 0, 1, 0, '', 'Professor'),
(7, 'kaushik', 'Dr Kaushik Deb', 'abc@gmail.com', 'kaushik', 2, 0, 1, 0, '', 'Professor'),
(8, 'asad', 'Dr. Asaduzzaman ', 'abc@gmail.com', 'asad', 2, 1, 1, 0, '', 'Professor'),
(9, 'pranab', 'Dr. Pranab Kumar Dhar', 'abc@gmail.com', 'pranab', 2, 0, 1, 0, '', 'Associate Professor'),
(10, 'mokammel', 'Dr. Md. Mokammel Haque', 'abc@gmail.com', 'mokammel', 2, 0, 1, 0, '', 'Assistant Professor'),
(12, 'mohasin', 'A.H.M. Mohashin', 'abc@gmail.com', 'mohasin', 1, 0, 1, 1104045, 'A', ''),
(13, 'jihan', 'Md. Jihan Al Rashid', 'abc@gmail.com', 'jihan', 1, 0, 1, 1104049, 'A', ''),
(14, 'antika', 'Antika Roy', 'abc@gmail.com', 'antika', 1, 0, 1, 1104042, 'A', ''),
(15, 'naima', 'Naima Sultana', 'abc@gmail.com', 'naima', 1, 0, 1, 1104060, 'A', ''),
(16, 'maruf', 'Abdullah Al Maruf', 'abc@gmail.com', 'maruf', 1, 0, 1, 1104029, 'A', ''),
(17, 'jesmin', 'Jesmin Akter', 'abc@gmail.com', 'jesmin', 1, 0, 1, 1104033, 'A', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
 ADD PRIMARY KEY (`assignment_id`,`student_id`,`date_time`);

--
-- Indexes for table `course`
--
ALTER TABLE `course`
 ADD PRIMARY KEY (`course_id`);

--
-- Indexes for table `course_assignment`
--
ALTER TABLE `course_assignment`
 ADD PRIMARY KEY (`assignment_id`);

--
-- Indexes for table `course_registration`
--
ALTER TABLE `course_registration`
 ADD PRIMARY KEY (`student_id`,`assignment_id`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
 ADD PRIMARY KEY (`dept_id`);

--
-- Indexes for table `user_info`
--
ALTER TABLE `user_info`
 ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `course_assignment`
--
ALTER TABLE `course_assignment`
MODIFY `assignment_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `user_info`
--
ALTER TABLE `user_info`
MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=18;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
