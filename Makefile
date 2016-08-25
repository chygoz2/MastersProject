
default: diamond

instances.txt:  java Generator 1 1000 5 10 
		ls -1 generated_graphs > instances.txt

include claw.mk diamond.mk k4.mk simplicial.mk triangle.mk


