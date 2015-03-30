<h2>AddCardSet</h2>

<p>The following script creates a CardSet and a AddCardSet.      </p>

<pre><code>    c
    d What is the capital of Ohio (# $columbus)

    +
    d Discover of the New World.

    * end
</code></pre>

<p>The CardSet, beginning with the command 'c', asks a question requiring a user <br />
response. The AddCardSet, beginning with the command '+', provides a probe or <br />
prompt, that is, a hint, for the user     </p>

<p>The question-CardSet arms the '+Add' button. Activation of the +Add button presents <br />
the AddCardSet along with its hint. Following the 'Next' button activation, <br />
the user is returned to the question-CardSet to complete the response.       </p>

<p>On the otherhand, the user can enter a response and activate the 'Next' button to <br />
skip the AddCardSet.     </p>

<p>The AddCardSet is always associated with the immediately preceding CardSet.  A <br />
CardSet may have more than one AddCardSet as the following script illustrates.   </p>

<pre><code>    c
    d What is the capital of Ohio (# $columbus)

    +
    d Discover of the New World.

    +
    d Begins with 'C' and has 8 letters

    * end
</code></pre>

<p>Termination of the last AddCardSet initiates the return or re-display of the <br />
question-CardSet. A single AddCardSet is also the last in the series.  The series <br />
may be terminated before the last AddCardSet by activation of the '+Add' button. <br />
This button acts as a toggle switch to start and to terminate the series. The '+Add' <br />
button remains   highlighted until the last AddCardSet is    </p>

<p>Script Program:   </p>

<p>The CardSet and AddCardSet are identical in almost all respects. The differ by the <br />
'button' parameter that each has.    </p>

<pre><code>    CardSet        button=0     No associated AddCardSet
    CardSet        button=1     Associated AddCardSet
    AddCardSet     button=2     Not last
    AddCardSet     button=99    Last AddCardSet
</code></pre>

<p>The top-level program 'script.scala' has two major program divisions:        </p>

<pre><code>    ParserValidator   detects script errors    
    BuildStructure    creates linked list hiearchy    

    ParserValidator    
        CommandMaker 
            Tags 'c', 'b', or '+' invoke CardCommand
        CardCommand (CardSetCommand)
            Validates logic expression; invokes CardScript
        CardScript  (CardSetScript)
            Create "CardSet" and "AddCardSet"
    BuildStructure
        CommandLoader   
            Principle task to create objects, like CardSet,
            and pass these objects there parameters.
        CommandStructure
            Build linked list structure where Notecard
            is the ancestor of all classes.
        ModifyAddCardSet
            Set 'button' parameter.
</code></pre>
