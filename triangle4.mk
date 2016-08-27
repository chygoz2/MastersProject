JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 listInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

triangle4: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_triangle_listing_result/$(i))

$(RESULTSDIR)/bruteforce_triangle_listing_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_triangle_listing_result
	$(JVM) bruteforce.listing.ListTriangles $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_triangle_listing_result/$*
