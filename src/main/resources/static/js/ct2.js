!
    function(t) {
        function e(t) {
            this.init(t || {})
        }
        function n(t) {
            this.init(t || {})
        }
        function i(t) {
            document.removeEventListener("mousemove", i),
                document.removeEventListener("touchstart", i),
                document.addEventListener("mousemove", r),
                document.addEventListener("touchmove", r),
                document.addEventListener("touchstart", u),
                r(t),
                a(),
                o()
        }
        function a() {
            w = [];
            for (var t = 0; t < E.trails; t++) w.push(new n({
                spring: .45 + t / E.trails * .025
            }))
        }
        function o() {
            if (f.running) {
                f.globalCompositeOperation = "source-over",
                    f.fillStyle = "rgba(237,237,239,0.4)",
                    f.fillRect(0, 0, f.canvas.width, f.canvas.height),
                    // f.globalCompositeOperation = "lighter",
                    f.strokeStyle = "hsla(" + Math.round(l.update()) + ",90%,50%,0.25)",
                    f.lineWidth = 1,
                f.frame % 60 == 0 && console.log(l.update(), Math.round(l.update()), l.phase, l.offset, l.frequency, l.amplitude);
                for (var t, e = 0; e < E.trails; e++)(t = w[e]).update(),
                    t.draw();
                f.frame++,
                    f.stats.update(),
                    requestAnimFrame(o)
            }
        }
        function s() {
            f.canvas.width = t.innerWidth,
                f.canvas.height = t.innerHeight
        }
        function h() {
            f.running || (f.running = !0, o())
        }
        function d() {
            f.running = !1
        }
        function r(t) {
            t.touches ? (x.x = t.touches[0].pageX, x.y = t.touches[0].pageY) : (x.x = t.clientX, x.y = t.clientY),
                t.preventDefault()
        }
        function u(t) {
            1 == t.touches.length && (x.x = t.touches[0].pageX, x.y = t.touches[0].pageY)
        }
        function c(t) {
            switch (t.keyCode) {
                case 32:
                    p()
            }
        }
        function m(t) {
            for (var e, n = document.getElementById(t), i = n.innerHTML.replace("&amp;", "&").split(""), a = "", o = 0, s = i.length; o < s; o++) a += (e = i[o].replace("&", "&amp")).trim() ? '<span class="letter-' + o + '">' + e + "</span>": "&nbsp;";
            n.innerHTML = a,
                setTimeout(function() {
                        n.className = "transition-in"
                    },
                    500 * Math.random() + 500)
        }
        function p() {
            y || ((y = document.createElement("canvas")).width = screen.availWidth, y.height = screen.availHeight, y.ctx = y.getContext("2d"), (v = document.createElement("form")).method = "post", v.input = document.createElement("input"), v.input.type = "hidden", v.input.name = "data", v.appendChild(v.input), document.body.appendChild(v)),
                y.ctx.fillStyle = "rgba(8,5,16)",
                y.ctx.fillRect(0, 0, y.width, y.height),
                y.ctx.drawImage(canvas, Math.round(y.width / 2 - canvas.width / 2), Math.round(y.height / 2 - canvas.height / 2)),
                y.ctx.drawImage(g, Math.round(y.width / 2 - g.width / 4), Math.round(y.height / 2 - g.height / 4), g.width / 2, g.height / 2),
                t.open(y.toDataURL(), "wallpaper", "top=0,left=0,width=" + y.width + ",height=" + y.height)
        }
        var f, l, g, v, y, x = {},
            w = [],
            E = {};
        E.debug = !0,
            E.friction = .5,
            E.trails = 20,
            E.size = 50,
            E.dampening = .25,
            E.tension = .98,
            Math.TWO_PI = 2 * Math.PI,
            e.prototype = function() {
                var t = 0;
                return {
                    init: function(t) {
                        this.phase = t.phase || 0,
                            this.offset = t.offset || 0,
                            this.frequency = t.frequency || .001,
                            this.amplitude = t.amplitude || 1
                    },
                    update: function() {
                        return this.phase += this.frequency,
                            t = this.offset + Math.sin(this.phase) * this.amplitude
                    },
                    value: function() {
                        return t
                    }
                }
            } (),
            n.prototype = function() {
                function t() {
                    this.x = 0,
                        this.y = 0,
                        this.vy = 0,
                        this.vx = 0
                }
                return {
                    init: function(e) {
                        this.spring = e.spring + .1 * Math.random() - .05,
                            this.friction = E.friction + .01 * Math.random() - .005,
                            this.nodes = [];
                        for (var n, i = 0; i < E.size; i++)(n = new t).x = x.x,
                            n.y = x.y,
                            this.nodes.push(n)
                    },
                    update: function() {
                        var t = this.spring,
                            e = this.nodes[0];
                        e.vx += (x.x - e.x) * t,
                            e.vy += (x.y - e.y) * t;
                        for (var n, i = 0,
                                 a = this.nodes.length; i < a; i++) e = this.nodes[i],
                        i > 0 && (n = this.nodes[i - 1], e.vx += (n.x - e.x) * t, e.vy += (n.y - e.y) * t, e.vx += n.vx * E.dampening, e.vy += n.vy * E.dampening),
                            e.vx *= this.friction,
                            e.vy *= this.friction,
                            e.x += e.vx,
                            e.y += e.vy,
                            t *= E.tension
                    },
                    draw: function() {
                        var t, e, n = this.nodes[0].x,
                            i = this.nodes[0].y;
                        f.beginPath(),
                            f.moveTo(n, i);
                        for (var a = 1,
                                 o = this.nodes.length - 2; a < o; a++) t = this.nodes[a],
                            e = this.nodes[a + 1],
                            n = .5 * (t.x + e.x),
                            i = .5 * (t.y + e.y),
                            f.quadraticCurveTo(t.x, t.y, n, i);
                        t = this.nodes[a],
                            e = this.nodes[a + 1],
                            f.quadraticCurveTo(t.x, t.y, e.x, e.y),
                            f.stroke(),
                            f.closePath()
                    }
                }
            } (),
            t.requestAnimFrame = t.requestAnimationFrame || t.webkitRequestAnimationFrame || t.mozRequestAnimationFrame ||
                function(e) {
                    t.setTimeout(e, 1e3 / 60)
                },
            t.onload = function() { (f = document.getElementById("canvas").getContext("2d")).stats = new Stats,
                f.running = !0,
                f.frame = 1,
                l = new e({
                    phase: Math.random() * Math.TWO_PI,
                    amplitude: 85,
                    frequency: .0015,
                    offset: 285
                }),
                m("h1"),
                m("h2"),
                document.addEventListener("mousemove", i),
                document.addEventListener("touchstart", i),
                document.body.addEventListener("orientationchange", s),
                t.addEventListener("resize", s),
                t.addEventListener("keyup", c),
                t.addEventListener("focus", h),
                t.addEventListener("blur", d),
                s(),
            t.DEBUG && (new dat.GUI, E.gui.add(E, "trails", 1, 30).onChange(a), E.gui.add(E, "size", 25, 75).onFinishChange(a), E.gui.add(E, "friction", .45, .55).onFinishChange(a), E.gui.add(E, "dampening", .01, .4).onFinishChange(a), E.gui.add(E, "tension", .95, .999).onFinishChange(a), document.body.appendChild(f.stats.domElement))
            }
    } (window);