DROP DATABASE IF EXISTS `ethicalaivalidation`;
CREATE DATABASE `ethicalaivalidation`;
USE `ethicalaivalidation`;
CREATE TABLE `company`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `companyname` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `principles`
(
    `id`            int(11)     NOT NULL AUTO_INCREMENT,
    `principlename` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `principlename_UNIQUE` (`principlename`)
);
CREATE TABLE `questiontype`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT,
    `type` int(11) NOT NULL,
    PRIMARY KEY (`id`)
);
CREATE TABLE `users`
(
    `id`            int(11)      NOT NULL AUTO_INCREMENT,
    `username`      varchar(50)  NOT NULL,
    `password`      varchar(100) NOT NULL,
    `email`         varchar(50)  NOT NULL,
    `usertype`      int(11)      NOT NULL,
    `firstname`     varchar(50)           DEFAULT NULL,
    `lastname`      varchar(50)           DEFAULT NULL,
    `companyid`     int(11)      NOT NULL,
    `address1`      varchar(50)           DEFAULT NULL,
    `address2`      varchar(50)           DEFAULT NULL,
    `phone`         varchar(50)           DEFAULT NULL,
    `createdtime`   timestamp    NOT NULL,
    `verifiedemail` int(11)      NOT NULL,
    `passwordtoken` longtext,
    `emailtoken`    longtext,
    `image`         varchar(50)  NOT NULL DEFAULT 'defaultimage.png',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_UNIQUE` (`username`),
    KEY `FK_Usercompany_Companyid_idx` (`companyid`),
    CONSTRAINT `FK_Usercompany_Companyid` FOREIGN KEY (`companyid`) REFERENCES `company` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
);
CREATE TABLE `projects`
(
    `id`           int(11)      NOT NULL AUTO_INCREMENT,
    `projectname`  varchar(100) NOT NULL,
    `description`  longtext,
    `createdtime`  timestamp    NOT NULL,
    `status`       int(11)      NOT NULL,
    `finishedtime` timestamp    NULL DEFAULT NULL,
    `creatorid`    int(11)           DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `projectname_UNIQUE` (`projectname`),
    KEY `FK_Projectcreatorid_Userid_idx` (`creatorid`),
    CONSTRAINT `FK_Projectcreatorid_Userid` FOREIGN KEY (`creatorid`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
);
CREATE TABLE `projectassign`
(
    `id`             int(11)   NOT NULL AUTO_INCREMENT,
    `projectid`      int(11)   NOT NULL,
    `supplierid`     int(11)   NOT NULL,
    `assigntime`     timestamp NOT NULL,
    `locked`         int(11)   NOT NULL,
    `lockedquestion` int(11)        DEFAULT NULL,
    `lockedtime`     timestamp NULL DEFAULT NULL,
    `updatetime`     timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Projectassignprojectid_Projectsid_idx` (`projectid`),
    KEY `FK_Projectassignsupplierid_Usersid_idx` (`supplierid`),
    CONSTRAINT `FK_Projectassignprojectid_Projectsid` FOREIGN KEY (`projectid`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Projectassignsupplierid_Usersid` FOREIGN KEY (`supplierid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `projectvalidation`
(
    `id`           int(11)   NOT NULL AUTO_INCREMENT,
    `projectid`    int(11)   NOT NULL,
    `validatorid`  int(11)   NOT NULL,
    `assignedtime` timestamp NOT NULL,
    `status`       int(11)   NOT NULL,
    `checknumber`  int(11)   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Projectvalidationprojectid_Projectsid_idx` (`projectid`),
    KEY `FK_Projectvalidationvalidatorid_Usersid_idx` (`validatorid`),
    CONSTRAINT `FK_Projectvalidationprojectid_Projectsid` FOREIGN KEY (`projectid`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Projectvalidationvalidatorid_Usersid` FOREIGN KEY (`validatorid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `segments`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `principleid` int(11)     NOT NULL,
    `segmentname` varchar(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `segmentname_UNIQUE` (`segmentname`),
    KEY `FK_Segmentsprincipleid_Principlesid_idx` (`principleid`),
    CONSTRAINT `FK_Segmentsprincipleid_Principlesid` FOREIGN KEY (`principleid`) REFERENCES `principles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `questions`
(
    `id`              int(11)  NOT NULL AUTO_INCREMENT,
    `segmentid`       int(11)  NOT NULL,
    `questioncontent` longtext NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Questionssegmentid_Segmentsid_idx` (`segmentid`),
    CONSTRAINT `FK_Questionssegmentid_Segmentsid` FOREIGN KEY (`segmentid`) REFERENCES `segments` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `subquestions`
(
    `id`           int(11)   NOT NULL AUTO_INCREMENT,
    `questionid`   int(11)   NOT NULL,
    `content`      longtext  NOT NULL,
    `questiontype` int(11)   NOT NULL,
    `createdtime`  timestamp NOT NULL,
    `level`        int(11)   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Subquestionsquestionid_Questionsid_idx` (`questionid`),
    KEY `FK_Subquestionsquestiontype_Questiontypeid_idx` (`questiontype`),
    CONSTRAINT `FK_Subquestionsquestionid_Questionsid` FOREIGN KEY (`questionid`) REFERENCES `questions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Subquestionsquestiontype_Questiontypeid` FOREIGN KEY (`questiontype`) REFERENCES `questiontype` (`id`) ON UPDATE CASCADE
);
CREATE TABLE `answer`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT,
    `subquesid` int(11) NOT NULL,
    `answer`    int(11) NOT NULL,
    `point`     int(11) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Answersubquesid_Subquestionsid_idx` (`subquesid`),
    CONSTRAINT `FK_Answersubquesid_Subquestionsid` FOREIGN KEY (`subquesid`) REFERENCES `subquestions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `ethicalconcerns`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `projectid`  int(11) NOT NULL,
    `subquesid`  int(11) NOT NULL,
    `questionid` int(11) NOT NULL,
    `answer`     longtext,
    `points`     int(11)          DEFAULT NULL,
    `finished`   int(11) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    KEY `FK_Ethicalconcernsprojectid_Projectsid_idx` (`projectid`),
    KEY `FK_Ethicalconcernsquestionid_Questionsid_idx` (`questionid`),
    KEY `FK_Ethicalconcernssubquesid_Subquestionsid_idx` (`subquesid`),
    CONSTRAINT `FK_Ethicalconcernsprojectid_Projectsid` FOREIGN KEY (`projectid`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Ethicalconcernsquestionid_Questionsid` FOREIGN KEY (`questionid`) REFERENCES `questions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Ethicalconcernssubquesid_Subquestionsid` FOREIGN KEY (`subquesid`) REFERENCES `subquestions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE `questionfeedback`
(
    `id`           int(11)   NOT NULL AUTO_INCREMENT,
    `projectid`    int(11)   NOT NULL,
    `validatorid`  int(11)   NOT NULL,
    `subquesid`    int(11)   NOT NULL,
    `createdindex` int(11)   NOT NULL,
    `content`      longtext  NOT NULL,
    `feedbacktime` timestamp NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Questionfeedbackprojectid_Projectsid_idx` (`projectid`),
    KEY `FK_Questionfeedbackvalidatorid_Usersid_idx` (`validatorid`),
    KEY `FK_Questionfeedbacksubquesid_Subquestionsid_idx` (`subquesid`),
    CONSTRAINT `FK_Questionfeedbackprojectid_Projectsid` FOREIGN KEY (`projectid`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Questionfeedbacksubquesid_Subquestionsid` FOREIGN KEY (`subquesid`) REFERENCES `subquestions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Questionfeedbackvalidatorid_Usersid` FOREIGN KEY (`validatorid`) REFERENCES `users` (`id`) ON UPDATE CASCADE
);
CREATE TABLE `questionstatus`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `projectid`   int(11) NOT NULL,
    `validatorid` int(11) NOT NULL,
    `subquesid`   int(11) NOT NULL,
    `status`      int(11) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    KEY `FK_Questionstatusprojectid_Projectsid_idx` (`projectid`),
    KEY `FK_Questionstatusvalidatorid_Usersid_idx` (`validatorid`),
    KEY `FK_Questionstatussubquesid_Subquestionsid_idx` (`subquesid`),
    CONSTRAINT `FK_Questionstatusprojectid_Projectsid` FOREIGN KEY (`projectid`) REFERENCES `projects` (`id`),
    CONSTRAINT `FK_Questionstatussubquesid_Subquestionsid` FOREIGN KEY (`subquesid`) REFERENCES `subquestions` (`id`),
    CONSTRAINT `FK_Questionstatusvalidatorid_Usersid` FOREIGN KEY (`validatorid`) REFERENCES `users` (`id`)
);
CREATE TABLE `segmentsummary`
(
    `id`          int(11)   NOT NULL AUTO_INCREMENT,
    `projectid`   int(11)   NOT NULL,
    `validatorid` int(11)   NOT NULL,
    `segmentid`   int(11)   NOT NULL,
    `summary`     longtext  NOT NULL,
    `createdtime` timestamp NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Segmentsummaryprojectid_Projectsid_idx` (`projectid`),
    KEY `FK_Segmentsummaryvalidatorid_Usersid_idx` (`validatorid`),
    KEY `FK_Segmentsummarysegmentid_Segmentsid_idx` (`segmentid`),
    CONSTRAINT `FK_Segmentsummaryprojectid_Projectsid` FOREIGN KEY (`projectid`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Segmentsummarysegmentid_Segmentsid` FOREIGN KEY (`segmentid`) REFERENCES `segments` (`id`) ON UPDATE CASCADE,
    CONSTRAINT `FK_Segmentsummaryvalidatorid_Usersid` FOREIGN KEY (`validatorid`) REFERENCES `users` (`id`) ON UPDATE CASCADE
);
CREATE TABLE `validatorfeedback`
(
    `id`          int(11)   NOT NULL AUTO_INCREMENT,
    `validatorid` int(11)   NOT NULL,
    `projectid`   int(11)   NOT NULL,
    `sendtime`    timestamp NOT NULL,
    `checkindex`  int(11)   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_Validatorfeedbackvalidatorid_Usersid_idx` (`validatorid`),
    KEY `FK_Validatorfeedbackprojectid_Projectsid_idx` (`projectid`),
    CONSTRAINT `FK_Validatorfeedbackprojectid_Projectsid` FOREIGN KEY (`projectid`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_Validatorfeedbackvalidatorid_Usersid` FOREIGN KEY (`validatorid`) REFERENCES `users` (`id`) ON UPDATE CASCADE
);














