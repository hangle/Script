<h1>Script to Test Commands</h1>

<p>The 'test' directory contains '*.nc' files to test script commands. <br />
For example, 'groupCmd.nc' script has the following CardSet:</p>

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

<p>The CardSet commands prints the script that are to be executed:</p>

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

<p>A test script will be created for the 8 types of command. The test <br />
script will have a structure showing the script between the <br />
'd' commands:   </p>

<pre>
        d ---------script--------

        d -----------------------
</pre>

<p>`</p>
