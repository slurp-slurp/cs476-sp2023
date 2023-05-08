# cs476-sp2023
- Code of final project for CS 476 Processing Big Data for Analytics Applications
- by Nathalia Xu
--------------------
| Table of Contents |
--------------------
1. Input files
2. Ingestion commands
3. Cleaning codes
4. Profiling codes
5. Analytic codes
6. Directory structure


------------------
| 1. Input files |
------------------
The input dataset is from https://archive.org/details/twitter_cikm_2010. 
The training dataset in it, which I use, contains 115,886 Twitter users and 3,844,612 tweets from them. Note that this is different from what I picked in my proposal. Per grader feedback that my original choice was small, I changed to this bigger dataset. 

The dataset is split into 2 files.
(1) original_tTweets.txt 
(2) original_user_location.txt
```
Schema:
original_tTweets
--------------------------
userID String
tweetID String
tweet String
createdAt date (won't use)
--------------------------

original_user_location
--------------------------
userID String (foreign key ref original_tTweets.userID)
location String
--------------------------
```

The two original files are cleaned. The 2 cleaned files are used for further analysis. 
Locations:
```
2 original files
	project/input/original/tweets/original_tTweets.txt
	project/input/original/user_location/original_user_location.txt
2 cleaned files
	project/input/cleaned/tweets/clean_tweets.txt
	project/input/cleaned/user_loc/clean_user_location.txt
```

-----------------------
| 2. Ingestion Commands |
------------------------
A. HOW TO BUILD
- Commands that puts files in HDFS. 
```
-mkdir and -put.
```

B. HOW TO RUN
```
Run directly in HDFS. 
```

C. WHERE TO FIND RESULTS
```
HDFS
project/input/original/tweets/original_tTweets.txt
project/input/original/user_location/original_user_location.txt
```

--------------------
| 3. Cleaning codes |
--------------------
A. HOW TO BUILD
- Two sets of MR cleaning codes are written to clean the two original datasets. Codes are located in /etl_code in the homework.

(1) The cleaning code in "/etl_code/Nathalia Xu/clean tweets" is for original_tTweets.txt.
    It fulfills the cleaning requirements in both hw7 and hw8. It does the following.
    1. drops unneeded columns
    2. drops badly formed rows
    3. text formatting - normalizes all tweets to lowercase
    4. new boolean column based on if tweet contains target text "anymore"

(2) The cleaning code in "/Users/apple/Desktop/project/etl_code/Nathalia Xu/clean user location" is for original_user_location.txt. 
    It fulfills the cleaning requirements in both hw7 and hw8 and does the following.
    1. drops badly formed rows
    2. text formatting - removes state from all rows to normalize the location format
    - no extra columns to drop since there are only two

B. HOW TO RUN
(1)
```
javac -classpath `yarn classpath` -d . CleanTMapper.java
javac -classpath `yarn classpath` -d . CleanTReducer.java
javac -classpath `yarn classpath`:. -d . CleanT.java
jar -cvf cleanT.jar *.class
hadoop jar cleanT.jar CleanT project/input/original/tweets/original_tTweets.txt project/output/cleaned/tweets
```


(2)
```
javac -classpath `yarn classpath` -d . CleanUMapper.java
javac -classpath `yarn classpath` -d . CleanUReducer.java
javac -classpath `yarn classpath`:. -d . CleanU.java
jar -cvf cleanU.jar *.class
hadoop jar cleanU.jar CleanU project/input/original/user_location/original_user_location.txt project/output/cleaned/user_loc
```
 
C. WHERE TO FIND RESULTS (need to concat all the files in each)
```
(1) project/output/cleaned/tweets
(2) project/output/cleaned/user_loc
sample concat: hdfs dfs -cat project/output/cleaned/tweets/*
```

--------------------
| 4. Profiling codes |
--------------------
A. HOW TO BUILD
- One set of MR profiling codes are written to profile the 4 input files. It counts the number of records in each file. 

B. HOW TO RUN
```
javac -classpath `yarn classpath` -d . CountRecsMapper.java
javac -classpath `yarn classpath` -d . CountRecsReducer.java
javac -classpath `yarn classpath`:. -d . CountRecs.java
jar -cvf countRecs.jar *.class
hadoop jar countRecs.jar CountRecs <input path> <output path>
```

C. WHERE TO FIND RESULTS
```
project/output/profile/cleaned/tweets
project/output/profile/cleaned/user_location
project/output/profile/original/tweets
project/output/profile/original/user_location
- you can also see the results in /screenshot.
```


--------------------
| 5. Analytic codes |
--------------------
A. HOW TO BUILD`
``
Analytics are done using Hive, so there is no code, just commands and queries. 4 Parts.
1.create and describe tables
	creates and describes 2 tables based on the 2 cleaned inputs.
2.find distinct	
	finds (1) distinct locations and (2) distinct user + tweet combinations from the joined table of the 2.
3. count text data
	counts (1) sentences with "anymore", (2) sentences with positive anymore, and (3) count of positive anymore grouped by location
4. other analytics I need
	shows the location and tweet content for tweets with positive anymore for linguistic analysis.
```

B. HOW TO RUN
See queries in /ana_code. Just run them in Hive.

C. WHERE TO FIND RESULTS
Given in time by Hive. Also in /screenshots.



-------------------------
| 6. Directory structure |
-------------------------
```
All files are located in /project, to which I have given access. I show the structure here.

/project
 	project/input
   		project/input/cleaned
			project/input/cleaned/tweets
				project/input/cleaned/tweets/clean_tweets.txt
			project/input/cleaned/user_loc
				project/input/cleaned/user_loc/clean_user_location.txt
   		project/input/original
			project/input/original/tweets
				project/input/original/tweets/original_tTweets.txt
			project/input/original/user_location
				project/input/original/user_location/original_user_location.txt
  	project/output
		project/output/cleaned
			project/output/cleaned/tweets
				(files part-r-00000 - part-r-00046)
			project/output/cleaned/user_loc
				(files part-r-00000 - part-r-00046)
		project/output/profile
			project/output/profile/cleaned
				project/output/profile/cleaned/tweets
					(file part-r-00000)
				project/output/profile/cleaned/user_location
					(file part-r-00000)
			project/output/profile/original
				project/output/profile/original/tweets
					(file part-r-00000)
				project/output/profile/original/user_location
					(file part-r-00000)
```
Also, structure of project directories.
```

/project_XU
	readMe.txt
	/ana_code
		hive.zip
	/data_ingest
		ingest.txt
	/etl_code
		/Nathalia Xu
			/clean tweets
				code, .class, .jar
			/clean user location
				code, .class, .jar
	/profiling_code
		/Nathalia Xu
			code, .class, .jar
	/screenshots
		(screenshots png files for all steps)

```
