JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 listInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

k44: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_k4_listing_result/$(i))

$(RESULTSDIR)/bruteforce_k4_listing_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_k4_listing_result
	$(JVM) bruteforce.listing.ListK4 $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_k4_listing_result/$*
