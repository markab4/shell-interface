# Shell Interface

This repository consists of a Java program that serves as a shell interface that accepts user commands and then executes each command in a separate process external to the Java virtual machine.

A shell interface provides the user with a prompt, after which the user enters the next command. 

### Description

This shell :
* creates an external process and executing the command in that process
* allows changing directories
* has a history feature
* works on both UNIX and Windows machines

The main() method presents the prompt ```jsh>``` (for java shell) and waits to read input from the user.
The program is terminated when the user enters ```Ctrl``` + ```C```.


### History Feature


This shell provides a history feature that allows users to see the history of commands they have entered and to rerun a command from that history. The history includes all commands that have been entered by the user since the shell was invoked.

For example, if the user entered the history command and saw as output:
 ```    
     0 pwd
     1 ls -l
     2 cat Prog.java
```
     
the history would list ```pwd``` as the first command entered, ```ls -l``` as the second command, and so on.
This program allows users to rerun commands from their history by supporting the following three techniques:
1. When the user enters the command history, the shell prints out the contents of the history of commands that have been entered into the shell, along with the command numbers.
2. When the user enters ```!!```, it runs the previous command in the history. If there is no previous command, it outputs an appropriate error message.
3. When the user enters ```!<integer value>```, it runs the ith command in the history. For example, entering ```!4``` would run the fourth command in the command history. Error checking is also done to ensure that the integer value is a valid number in the command history.