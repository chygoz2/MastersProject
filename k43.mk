JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 listInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

k43: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_k4_listing_result/$(i))

$(RESULTSDIR)/efficient_k4_listing_result/%:
	mkdir -p $(RESULTSDIR)/efficient_k4_listing_result
	$(JVM) efficient.listing.ListK4 $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_k4_listing_result/$*
