# geo-grapher
Summary: GeoGrapher is a 2D parametric graphing program. The user can define equations for x and y as a funciton of the variable n, as well as a variable t which updates over time to transform the curve. Values and expressions are enterred as plain text strings. The interpretation of these strings is meant to mimic, as closely as possible, the way mathematical expressions are usually written down and understood in plain text. Additionally,  the program hosts several built-in functions which can help the user define the parametric equations.

Code Files:
- GeoGrapher.java
- GeoGrapherApplication.java
- Expression.java
- Token.java
- MathError.java
- ExpressionTree.java
- ExpressionTreeNode.java
- Operation.java
- DefOps.java
- Function.java
- FunctionList.java
- FunctionAlreadyInListException.java
- Constant.java
- Functs.java
- Trig.java
- HyperTrig.java

Additional Files:
- instructions.txt
- functions.txt

External Dependencies:
- java.awt.Color
- java.awt.Graphics
- java.awt.Image
- java.util.Iterator
- java.util.Scanner
- java.util.ArrayList
- java.util.logging.Level
- java.util.logging.Logger
- java.util.EmptyStackException
- java.util.Stack
- java.util.Comparator
- java.io.File
- java.io.FileNotFoundException
- java.net.URISyntaxException
- java.net.URL
- javax.swing.JFrame

Executable:
- GeoGrapherApplication

Steps to Run:

1.) Place all ".java" files in a directory called "geographer"

2.) Navigate to the parent directory of "geographer"

3.) Run the command "javac geographer\GeoGrapherApplication.java" to compile

4.) Run the command "java geographer.GeoGrapherApplication" to execute
