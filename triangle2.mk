JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

triangle: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_triangle_detection_result/$(i))

$(RESULTSDIR)/bruteforce_triangle_detection_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_triangle_detection_result
	$(JVM) easydetection/DetectTriangle $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_triangle_detection_result/$*

