<h1>Script to Test Commands</h1>

<p>The Test directory attempts to discover execution problems with <br />
the Script program and with the Notecard program. It puts <br />
each command to an exhaustive review.  The files (.struct) of <br />
the Test directory are executed by the Notecard program 'card'. <br />
The 'card' program will reveal problems either with itself or <br />
with the Script program failure to detect a syntax error.  </p>

<p>The development of script Test files is a work in progress. <br />
To date, the following Test files are available:  </p>

<pre><code>    Display command's  /size &lt;n&gt;/ components.
        letterSize.struct
    Group command
        groupCmd.struct
</code></pre>

<h2>Display command's /size <n>/  </h2>

<p>The Display component /size <n>/ changes the letter size. <br />
The command:  </p>

<pre><code>    d /size 24/
</code></pre>

<p>changes the default letter size to 24. <br />
The 'letterSize.struct' shows the script to be executed <br />
by the next Card Set.   </p>

<h2>GroupCmd Example</h2>

<p>For example, 'groupCmd.nc' script has the following CardSet: </p>

<pre>
        c
        d ---------script----------
        d    g (1)=(1)
        d    d This line is displayed
        d    ge
        d    d This line is NOT displayed
        d ----------------------
        g (1)=(1)
        d This line is displayed
        ge
        d This line is NOT displayed
        c ...
</pre>

<p>The CardSet commands prints the script that are to be executed: </p>

<pre>
        d ---------script----------
            d    g (1)=(1)
            d    d This line is displayed
        d    ge
        d    d This line is NOT displayed
            d ----------------------
</pre>

<p>For the following script commands, if the line 'This line is <br />
displayed' fails to appear, or the line 'This line is NOT printed' <br />
is printed, then a problem exists for the Group command:  </p>

<pre>
        g (1)=(1)
        d This line is displayed
        ge
        d This line is NOT displayed
        c ...
</pre>

<p>`</p>
