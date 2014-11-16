<hr />

<h2>Test Directory:  </h2>

<p>The execution of the script in the DEMO directory:  </p>

<pre><code>    scala card demo/card
</code></pre>

<p>reveals features of the Notecard capabilities.  However, it <br />
does not exercise the complete range of capabilities.  The <br />
DEMO directory just highlights the major Notecard features. <br />
For the user, it illustrates the potential of the Notecard <br />
system.  The TEST directory exercises every feature of the <br />
system. Its purpose is not a tutorial; rather, its a means <br />
to reveal or find errors and limitations in the code.   </p>

<p>It is expected that each script in the TEST directory <br />
will grow to exercise every combination script elements <br />
in serarch of a failure, either leading to code correction <br />
or a controlled exception in the script program. The <br />
focus of the TEST directory is to sresss the system. <br />
The TEST directory also serves a regression test.   </p>

<p>To date, the following TEST scripts are available:   </p>

<pre><code>appearance.nc       
    Example:
        d 5/4/color red/Sorry wrong answer
        d now is (%%/color blue/for all good) men
groupCmd.nc
    Example:
        g (flag)=(true)
        ge
inputFields.nc
    Example:
        d Enter (# $name)
        d (# $one)  (# $two)  (# $three)
letterSize.nc       Note, component of appearance
    Example:
        d 5/4/size 20/Sorry wrong answer
        d now is (%%/size 10/for all good) men
loadDictionary.nc
    Example:
        l
        a $count=0
        a $flag=false
</code></pre>

<h2>Execution:</h2>

<p>Execution of the sript appearance.nc  must  be compiled in <br />
the Script/src directory:    </p>

<pre><code>    scala script test/appearance
</code></pre>

<p>The output file of the 'scala script text/appearance' is <br />
'appearance.struct'.  This file is executed in the Notecard <br />
src directory:</p>

<pre><code>    scala card test/appearance
</code></pre>
