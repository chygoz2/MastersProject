JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

k4: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_k4_detection_result/$(i))

$(RESULTSDIR)/efficient_k4_detection_result/%:
	mkdir -p $(RESULTSDIR)/efficient_k4_detection_result
	$(JVM) efficientdetection/DetectK4 $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_k4_detection_result/$*
