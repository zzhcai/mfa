# SWEN90006 Assignment 1: Testing the MFA Server

## Overview

This assignment deals with input partitioning, boundary-value analysis, and control-flow testing; and a bit of mutation analysis.

You are given a specification and a program that implements that specification. The aim of this assignment is to test the program using the different techniques, and to analyse the difference between them.

You are expected to derive and compare test cases, but you are not expected to debug the program.

The assignment is part laboratory exercise because you are expected to write a JUnit driver program to run your test cases automatically. Some exploration may be needed here. The assignment is also part analysis exercise as you are expected to apply the testing techniques to derive your test cases, and to compare them. Finally, the assignment is also part competition, as your solutions to various tasks will be evaluated against all other submissions in a small tournament to measure its effectiveness and completeness.

The assignment is worth 20% of your final mark.

## Your tasks

### Task 1 -- Equivalence partitioning

Using the specification, apply equivalence partitioning to derive equivalence classes for the following methods in the API: `register`, `login`, `respondToPushNotification`, and `getData`.

Document your equivalence partitioning process for each method using only test template trees, listing the assumptions that you make (if any). You should have four trees: one for each method. You will be marked *only* on your test template trees (plus any assumptions listed), so ensure that they are clear and concise. 

You can omit some nodes to improve readability, provided that it is clear what you intend. For example, if I was testing a book store and I wanted to test all seven Harry Potter books, I would create nodes for 1 and 7, and then use ``\ldots'' in between them to represent the other five books.

Note that as part of your input domain, you will have to consider the instance variables. These are not parameters to any of the methods, but they are *inputs*.

Do your set of equivalence classes cover the input space?  Justify your claim.

### Task 2 -- Junit test driver for equivalence partitioning

Select test cases associated with your equivalence classes, and implement these in the JUnit test driver named `tests/swen90006/mfa/PartitioningTests.java`. Use *one* JUnit test method for each equivalence class. For each test, clearly identify from which equivalence class it has been selected. Push this script back to your git repository.

Include this as Appendix A of your submission.

NOTE: When implementing tests for one method, you may use other methods to check that the first method has worked as expected. Similarly, sometimes you may be required to execute other methods in the class to get the instance into a state that is testable (see the example in `PartitioningTests.java`).

### Task 3 -- Boundary-value analysis

Conduct a boundary-value analysis for your equivalence classes. Show your working for this. Select test cases associated with your boundary values.

### Task 4 -- Junit test driver for boundary-value analysis

Implement your boundary-value tests in the JUnit test driver called `test/swen90006/mfa/BoundaryTests.java`. As before, use *one* JUnit test method for each test input.  Push this script back to you git repository.

Include this as Appendix B of your submission.

Note that the BoundaryTests JUnit script inherits PartitioningTest, which means all tests from PartitioningTests are including in BoundaryTests.  A JUnit test is just a standard public Java class! You may choose to remove this inheritance, but you may also use it to your advantage to make the BoundaryTest script easier to create. Overriding an existing test will replace it in the BoundaryTest sscript.

### Task 5 -- Multiple-condition coverage

Calculate the coverage score of your two test suites (equivalence partitioning and boundary-value analysis) using *multiple-condition coverage* each the four methods, as well as those methods in `MFA.java` that they call; that is, `isAuthenticated`.

Show your working for this coverage calculation in a table that lists each test objective (that is, each combination for multiple-condition coverage) and one test that achieves this, if any.

NOTE: You do NOT need to draw the control-flow graph for your solution.

You will receive marks for deriving correct coverage scores and showing how you come to this score. No marks are allocated for having a higher coverage score.

### Task 6 -- Mutation selection

Derive five *non-equivalent* mutants for the MFA class  using only the nine Java mutation operators in the subject notes. These mutants should be difficult to find using testing. Insert each of these mutants into the files `programs/mutant-1/swen90006/mfa/MFA.java`, `programs/mutant-2/swen90006/mfa/MFA.java`, etc.

All five mutants must be non-equivalent AND each mutant must be killed by at least one test in your JUnit BoundaryTest script to demonstrate that they are non-equivalent. They must only be in code that is executed when calling one of the four methods tested in Task 1.

Importantly, do not change anything else about the mutant files except for inserting the mutant.

Each mutant must change exactly one line of `MFA.java` for each version `mutant-1`, `mutant-2`, etc.

### Task 7 -- Comparison

Compare the two sets of test cases (equivalence partitioning and boundary-value analysis) and their results. Which method did you find was more effective and why? You should consider  the coverage of the valid input/output domain, the  coverage achieved, and the mutants it kills. Limit your  comparison to half a page. If your comparison is over half a page, you will be marked only on the first half page.

## The System: Multi-factor authentication (MFA)

The system that you will test is a simple multi-factor authentication (MFA) server. Strictly speaking, it implemented a two-factor authentication using: (a) passwords; and (b) access to physical devices. Once a person has authenticated, they can add and read generic data records.

A description of the methods for MFA and its implementation can be found in the file `programs/original/swen90006/mfa/MFA.java`.

For simplicity for the assignment, the database is implemented as a Java data structure.

### Building and running the program

The source code has been successfully built and tested on JDK 14 but should also work with some earlier versions of Java.

The file `build.xml` contains an Ant build script, should you choose to use it. The README.md file in the top-level folder has instructions for using this.

There are two JUnit test scripts in `test/swen90006/mfa/`. You will need to modify each of these to complete the tasks. You can run these by compiling and running as a class, but you will need to include the library files in the `lib/` directory on your classpath.

To compile, run:

`ant compile_orig`

To run the test scripts on the original (hopefully non-faulty) implementation, use:

`ant test -Dprogram="original" -Dtest="PartitioningTests"`

or

`ant test -Dprogram="original" -Dtest="BoundaryTests"`

To run a test script on the first mutant, use:

`ant test -Dprogram="mutant-1" -Dtest="BoundaryTests"`

Results for the tests can be found in the `results/` folder.

To clean all class files, run: `ant clean`

**NOTE**: If you find any functional faults in the implementation, please let us know via the discussion board. We will correct the fault and ask that everyone pull changes. There are not intended to be any faults in the implementation, but software engineering is hard!

## Marking criteria

As part of our marking, we will run your *boundary-value analysis* JUnit scripts on everyone else's mutants, as well as four mutants selected by the subject staff. You will receive marks for killing other mutants as well as for deriving mutants that are hard to kill. This will contribute 6 marks to the total.

| Criterion  | Description  | Marks  |
|---|---|---|
| Equivalence partitioning  | Clear evidence that partitioning the input space to find equivalence classes has been done systematically and correctly. Resulting equivalence classes are disjoint and cover the appropriate input space  | 6 |
| Boundary-value analysis | Clear evidence that boundary-value analysis has been applied systematically and correctly, and all boundaries, including on/off points, have been identified | 3 |
| Control-flow analysis    | Clear evidence that measurement of the control-flow criterion has been done systematically and correctly | 2     |
|                          | Clear and succinct justification/documentation of which test covers each objective | 2     |
| Discussion               | Clear demonstration of understanding of the topics used in the assignment, presented in a logical manner | 1     |
| JUnit tests              | Unit scripts implement the equivalence partitioning and boundary-value tests, and find many mutants, including the staff mutants | 4     |
| Mutants                  | Selected mutants are valid mutants and are difficult to find using tests | 2     |
| **Total** |   | 20 |

For the JUnit tests, each of the staff mutants kills is awarded 0.5 mark.

For the remaining 2 marks, the score for these will be calculated using the following formula:

junit_score = (k/T) / (ln(N) + 10)

where N iis the number of tests in your test suite, k is the number of mutants that your test suite kills, and T is the maximum number of mutants killed by any other JUnit test suite. This ensures that equivalent mutants are not counted.. The entire pool of mutants are the mutants from all other submissions. Therefore, your score is the mutant score, divided by ln(N)+10, which incentivises smaller test suites. This incentive is to resist the urge to submit a test suite of thousands of tests with the hope of increasing the score.. The maximum possible score is 0.1, scaled to be out of 2.0.

For the mutants, the score is:

mutant_score = (sum_i=1..M sum_j=1..N  a_i,j) / T


in which M is the total number of your mutants, N is the total number of other people's test suites, a_i,j=1 if mutant *i* is still alive after executing test suite *j*, and T =< M+N is the highest number of mutants still alive by any student in the class. This is then scaled to be out of 2. Therefore, your score is the inverse of the mutant score of all other students' test suites on your mutants, which incentivises you to submit hard-to-find mutants, while T normalises the score to ensure that everyone is rewarded for good mutants.

**Important**: We determine that a mutant is found when JUnit contains a failed test. Because of this, if a JUnit fails a test when applied to the original source code, it will fail on everyone else's mutants, giving people a 100% score. As such, **JUnit suites that fail on the original source code emulator will not be awarded any marks**. As noted above, if you find any faults in the original source code, please let us know via the discussion board. 

## Submission instructions

### JUnit script submission 
For the JUnit test scripts, we will clone everyone's Gitlab repository at the due time. We will mark the latest version on the main  branch of the repository. To have any late submissions marked, please email Tim ([`tmiller@unimelb.edu.au`](mailto:tmiller@unimelb.edu.au)) to let him know to pull changes from your repository.

Some important instructions:

1. Do NOT change the package names in any of the files.
2. Do NOT change the directory structure.
3. Do NOT add any new files: you should be able to complete the assignment without adding any new source files.

JUnit scripts will be batch run automatically, so any script that does not follow the instructions will not run and will not be awarded any marks.


### Report submission
For the remainder of the assignment (test template tree, boundary-value analysis working, coverage, and discussion) submit a PDF file using the links on the subject Canvas site. Go to the SWEN90006 Canvas site, select *Assignments* from the subject menu, and submit in *Assignment 1 report*.


## Tips

Some tips to managing the assignment, in particular, the equivalence partitioning:

1. Ensure that you understand the notes *before* diving into the assignment. Trying to learn equivalence partitioning or boundary-value analysis on a project this size is difficult. If you do not understand the simple examples in the notes, the understanding will not come from applying to a more complex example.

2. Keep it simple: don't focus on what you think we want to see --- focus on looking for good tests and then documenting them in a systematic way.  That IS what we want to see.
	
3. Focus on the requirements: as with any testing effort, focus your testing on the requirements, NOT on demonstrating the theory from the notes. Simply look at each requirement and see which guidelines should apply.

4. If you cannot figure out how to start your test template tree, just start listing tests that you think are important. Once you have a list, think about putting them into a tree.

### Late submission policy

If you require an extension, please contact Tim ([`tmiller@unimelb.edu.au`](mailto:tmiller@unimelb.edu.au)) to discuss. Having assessments due for other subjects is not a valid reason for an extension.

By default, everyone is implicitly granted an extension of up to 7 days, but with a penalty of 10% (2 marks) per day that the assignment is submitted late. So, if you are falling behind, you may want to consider submitted 1-2 days late if you feel you can do enough to make up the 2-4 marks. 

If you submit late, email Tim to let him know so he can pull the changes from the repository.

### Academic Misconduct

The University academic integrity policy (see [https://academicintegrity.unimelb.edu.au/](https://academicintegrity.unimelb.edu.au/) applies. Students are encouraged to discuss the assignment topic, but all submitted work must represent the individual's understanding of the topic. 

The subject staff take academic misconduct very seriously. In this subject in the past, we have successfully prosecuted several students that have breached the university policy. Often this results in receiving 0 marks for the assessment, and in some cases, has resulted in failure of the subject. 

### Originality Multiplier

For work that we find is similar to another submission or information found online, an originality multiplier will be applied to the work.  For example, if 20% of the assessment is deemed to have been taken from another source, the final mark will be multiplied by 0.8.
