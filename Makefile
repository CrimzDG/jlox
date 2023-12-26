build:
	javac lox/*.java

generate:
	javac tool/*.java
	java tool/GenerateAst lox/

ungen:
	rm tool/*.class

clean:
	rm lox/*.class

