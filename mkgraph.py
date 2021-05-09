#!/bin/python
import matplotlib.pyplot as plt
import networkx as nx

G = nx.Graph()
locNum,conNum = input().split()
for i in range(int(conNum)):
    a,b = input().split()
    G.add_edge(a,b)

sickNum = int(input())
sick = []
for i in range(int(sickNum)):
    sick.append(input().split()[0])

# Draw normal nodes
nx.draw_kamada_kawai(G,node_size=600,
        node_color="#D8DEE9",
        nodelist=list(filter(lambda a: a not in sick ,G.nodes)))

# Draw nodes from infected houses
nx.draw_kamada_kawai(G,nodelist = sick,
        with_labels=True,node_size=600,
        node_color="#81A1C1",font_color="#2E3440",
        font_family="Mononoki Nerd Font")

plt.savefig("graph.png")
