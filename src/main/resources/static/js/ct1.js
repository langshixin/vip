var Stats = function() {
    var e = Date.now(),
        t = e,
        n = 0,
        i = 1 / 0,
        a = 0,
        d = 0,
        l = 1 / 0,
        o = 0,
        s = 0,
        r = 0,
        c = document.createElement("div");
    c.id = "stats",
        c.addEventListener("mousedown",
            function(e) {
                e.preventDefault(),
                    g(++r % 2)
            },
            !1),
        c.style.cssText = "width:80px;opacity:0.9;cursor:pointer";
    var p = document.createElement("div");
    p.id = "fps",
        p.style.cssText = "padding:0 0 3px 3px;text-align:left;background-color:#002",
        c.appendChild(p);
    var h = document.createElement("div");
    h.id = "fpsText",
        h.style.cssText = "color:#0ff;font-family:Helvetica,Arial,sans-serif;font-size:9px;font-weight:bold;line-height:15px",
        h.innerHTML = "FPS",
        p.appendChild(h);
    var f = document.createElement("div");
    for (f.id = "fpsGraph", f.style.cssText = "position:relative;width:74px;height:30px;background-color:#0ff", p.appendChild(f); 74 > f.children.length;)(v = document.createElement("span")).style.cssText = "width:1px;height:30px;float:left;background-color:#113",
        f.appendChild(v);
    var x = document.createElement("div");
    x.id = "ms",
        x.style.cssText = "padding:0 0 3px 3px;text-align:left;background-color:#020;display:none",
        c.appendChild(x);
    var m = document.createElement("div");
    m.id = "msText",
        m.style.cssText = "color:#0f0;font-family:Helvetica,Arial,sans-serif;font-size:9px;font-weight:bold;line-height:15px",
        m.innerHTML = "MS",
        x.appendChild(m);
    var u = document.createElement("div");
    for (u.id = "msGraph", u.style.cssText = "position:relative;width:74px;height:30px;background-color:#0f0", x.appendChild(u); 74 > u.children.length;) {
        var v = document.createElement("span");
        v.style.cssText = "width:1px;height:30px;float:left;background-color:#131",
            u.appendChild(v)
    }
    var g = function(e) {
            switch (r = e) {
                case 0:
                    p.style.display = "block",
                        x.style.display = "none";
                    break;
                case 1:
                    p.style.display = "none",
                        x.style.display = "block"
            }
        },
        y = function(e, t) {
            e.appendChild(e.firstChild).style.height = t + "px"
        };
    return {
        REVISION: 11,
        domElement: c,
        setMode: g,
        begin: function() {
            e = Date.now()
        },
        end: function() {
            var r = Date.now();
            return n = r - e,
                i = Math.min(i, n),
                a = Math.max(a, n),
                m.textContent = n + " MS (" + i + "-" + a + ")",
                y(u, Math.min(30, 30 - n / 200 * 30)),
                s++,
            r > t + 1e3 && (d = Math.round(1e3 * s / (r - t)), l = Math.min(l, d), o = Math.max(o, d), h.textContent = d + " FPS (" + l + "-" + o + ")", y(f, Math.min(30, 30 - d / 100 * 30)), t = r, s = 0),
                r
        },
        update: function() {
            e = this.end()
        }
    }
};