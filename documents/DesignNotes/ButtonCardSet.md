<h2>ButtonCardSet</h2>

<p>The following script creates a CardSet <br />
and a ButtonCardSet.   </p>

<pre><code>c
d What is the capital of Ohio (# $columbus)
b
d Discover of the New World.
* end
</code></pre>

<p>The CardSet, beginning with the command 'c', asks a <br />
question requiring a user response. The ButtonCardSet, <br />
beginning with the command 'b', provides a probe or <br />
prompt, that is, a hint, for the user   </p>

<p>The question-CardSet arms the '+Add' button. Activation <br />
of the +Add button presents the ButtonCardSet along <br />
with its hint. Following the 'Next' button activation, <br />
the user is returned to the question-CardSet to <br />
complete the response.       </p>

<p>On the otherhand, the user can enter a response and <br />
activate the 'Next' button to skip the ButtonCardSet.   </p>

<p>The ButtonCardSet is always associated with the <br />
immediately preceding CardSet.  A CardSet may have <br />
more than one ButtonCardSet as the following script <br />
illustrates.   </p>

<pre><code>c
d What is the capital of Ohio (# $columbus)
b
d Discover of the New World.
b
d Begins with 'C' and has 8 letters
* end
</code></pre>

<p>Termination of the last ButtonCardSet initiates the   </p>

<p>return or re-display of the question-CardSet. A <br />
single ButtonCardSet is also the last in the series. <br />
The series my be terminated before the last <br />
ButtonCardSet by activation of the '+Add' button. <br />
This button acts as a toggle switch to start and to <br />
terminate the series. The '+Add' button remains <br />
highlighted until the last ButtonCardSet is <br />
encountered.   </p>

<p>Script Program:   </p>

<p>The CardSet and ButtonCardSet are identical in almost <br />
all respects. The differ by the 'button' parameter that <br />
each has.    </p>

<pre><code>CardSet        button=0     No associated ButtonCardSet
CardSet        button=1     Associated ButtonCardSet
ButtonCardSet  button=2     Not last
ButtonCardSet  button=99    Last ButtonCardSet
</code></pre>

<p>'script.scala' has two major program divisions:        </p>

<pre><code>ParserValidator   detects script errors    
BuildStructure    creates linked list hiearchy    

ParserValidator    
    CommandMaker 
        Tags 'c', 'b', or '+' invoke CardCommand
    CardCommand (CardSetCommand)
        Validates logic expression; invokes CardScript
    CardScript  (CardSetScript)
        Create "CardSet" and "ButtonCardSet"
BuildStructure
    CommandLoader   
        Principle task to create objects, like CardSet,
        and pass these objects there parameters.
    CommandStructure
        Build linked list structure where Notecard
        is the ancestor of all classes.
    ModifyButtonCardSet
        Set 'button' parameter.
</code></pre>
