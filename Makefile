build:
	javac lox/*.java

generate:
	javac tool/*.java

ungen:
	rm tool/*.class

clean:
	rm lox/*.class

