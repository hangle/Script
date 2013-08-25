<h1>Text Appearance and .ini file</h1>

<p>The visual features of Notecard text, as well as the size of the Notecard window, <br />
can be altered.  When unchanged, the features default to the following values:   </p>

<pre><code>    Notecard window height   300  
    Notecard window width    400
    Text size                14
    Text color               black
    Text type                normal or 0
    Font type                TimesRoman
    Input field length       10 
    Input field limit        99
</code></pre>

<p>The Notecard window height and width, along with the text size can be changed <br />
by the '*' commands placed at the beginning of the .nc file, for example:  </p>

<pre><code>    * height 600
    * width 500
    * size 22
    c
    d first and only card
    * end
</code></pre>

<p>The file 'appearance.ini' will also override the default values.  An example <br />
contents of the '.ini' file is shown   </p>

<pre><code>    height=550
    width=450
    style=0
    size=16
    length=5
</code></pre>

<p>The keys (height, width, style, size, length) are followed by values that are <br />
separated by the '=' symbol.     </p>

<p>The 'appearance.ini' file are found in two directories that hold the '.nc' file:  </p>

<pre><code>    path directory
    local directory
</code></pre>

<p>For example, the '.nc' files illustrating the features of the script commands <br />
are in the 'demo' directory:   </p>

<pre><code>    scala card demo/card
</code></pre>

<p>The file 'card.nc' has the path 'demo'.  The execution of 'myfile.nc' is in the <br />
local directory, for example:  </p>

<pre><code>    scala card myfile
</code></pre>

<p>When 'appearance.ini' files are in both 'path' and 'local' directories, the '.ini' <br />
file in the 'path' directory takes precident.  </p>
