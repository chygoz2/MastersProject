
default: claw.mk diamond.mk k4.mk simplicial.mk triangle.mk claw2.mk diamond2.mk k42.mk simplicial2.mk triangle2.mk

instances.txt:  java Generator 1 1000 5 10 
		ls -1 generated_graphs > instances.txt

include claw.mk diamond.mk k4.mk simplicial.mk triangle.mk claw2.mk diamond2.mk k42.mk simplicial2.mk triangle2.mk


