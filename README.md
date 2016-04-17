# schism

Let's try crowdsourced supervised reliable similarity judgments.

Goals:
 * reliable, less arbitrary measure by which to evaluate general-purpose word representations
 * enough data to learn word representations (that can capture similarity) in a supervised way
 * environment in which to experiment with different underlying distributed models of words

Tasks:
 * densely annotate small corpus of words with comparative similarity
 * compare inter-annotator agreement on comparative similarity to that on e.g. WordSim353
 * try evaluating common general-purpose vectors on this set
 * design basic systems to learn word representations that produce comparative similarity judgments
 * leverage these word representations to build systems that can play the game
 * build the game into a web interface so users can play with it
 * deploy the web app, attempt to go viral and collect lots of data
 * analyze crowd data and evaluate common general-purpose vectors with resulting new metric

Extra:
 * explore other bilexical relations (hypernymy, meronymy, co-hyponymy). Consider BLESS dataset.