/* ElectroMart – Dark ROG micro-interactions */
(function () {
  "use strict";
  const on = (el, evt, fn, opts) => el && el.addEventListener(evt, fn, opts || false);
  const qs = (s, root = document) => root.querySelector(s);
  const qsa = (s, root = document) => Array.from(root.querySelectorAll(s));
  const throttle = (fn, wait = 100) => { let t=0, tm; return (...a)=>{const n=Date.now(); if(n-t>=wait){t=n; fn(...a);} else {clearTimeout(tm); tm=setTimeout(()=>{t=Date.now(); fn(...a);}, wait-(n-t));}}; };
  const reduced = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (reduced) document.documentElement.classList.add('reduced-motion');

  // Reveal-in
  (function(){
    const els = qsa('.product-card, .brand-pill, .cat-tile');
    if (!els.length || reduced) return;
    els.forEach(el => el.classList.add('reveal-wait'));
    const io = new IntersectionObserver(es=>{
      es.forEach(e=>{ if(e.isIntersecting){ e.target.classList.add('reveal-in'); io.unobserve(e.target);} });
    }, {threshold:.08});
    els.forEach(el=> io.observe(el));
  })();

  // Navbar shadow
  (function(){
    const nav = qs('.glass-nav'); if(!nav) return;
    const apply = ()=> nav.classList.toggle('scrolled', window.scrollY > 4);
    apply(); on(window, 'scroll', throttle(apply, 80), {passive:true});
  })();

  // Tooltips
  (function(){
    if (!window.bootstrap) return;
    qsa('[data-bs-toggle="tooltip"]').forEach(el => new bootstrap.Tooltip(el));
  })();

  // Smooth anchor
  (function(){
    on(document, 'click', e=>{
      const a = e.target.closest('a[href^="#"]'); if(!a) return;
      const id = a.getAttribute('href'); if(!id || id==='#') return;
      const el = qs(id); if(!el) return;
      e.preventDefault();
      el.scrollIntoView({behavior: reduced ? 'auto' : 'smooth', block:'start'});
    });
  })();

  // Lazy images fallback
  (function(){
    const imgs = qsa('img[data-src]'); if(!imgs.length) return;
    if ('loading' in HTMLImageElement.prototype) {
      imgs.forEach(img => { img.loading='lazy'; img.src=img.dataset.src; img.removeAttribute('data-src'); });
      return;
    }
    const io = new IntersectionObserver(es=>{
      es.forEach(e=>{
        if(e.isIntersecting){
          const img = e.target; img.src = img.dataset.src; img.loading='lazy'; img.removeAttribute('data-src');
          io.unobserve(img);
        }
      });
    }, {rootMargin:'200px 0', threshold:.01});
    imgs.forEach(img=> io.observe(img));
  })();
  // ========== FLASH DEALS ==========

window.ElectroMart = window.ElectroMart || {};

ElectroMart.initFlashRail = function(){
  const wrap = document.getElementById('flashRailWrapper');
  if(!wrap) return;
  const rail = wrap.querySelector('.flash-rail');
  const prev = wrap.querySelector('.flash-btn.prev');
  const next = wrap.querySelector('.flash-btn.next');
  const step = () => Math.max(rail.clientWidth * 0.9, 300);

  prev?.addEventListener('click', () =>
    rail.scrollBy({ left: -step(), behavior: 'smooth' })
  );
  next?.addEventListener('click', () =>
    rail.scrollBy({ left:  step(), behavior: 'smooth' })
  );
};

ElectroMart.startCountdown = function(){
  const cd = document.getElementById('flashCountdown');
  if(!cd) return;
  const pad = n => String(n).padStart(2,'0');

  const endStr = cd.dataset.end && cd.dataset.end.trim();
  // Mặc định: hết ngày hôm nay 23:59:59
  const end = endStr ? new Date(endStr) : new Date(new Date().setHours(23,59,59,0));

  function tick(){
    let t = end.getTime() - Date.now();
    if (t < 0) t = 0;
    const h = Math.floor(t/3.6e6);
    const m = Math.floor((t%3.6e6)/6e4);
    const s = Math.floor((t%6e4)/1000);
    cd.querySelector('[data-unit="h"]').textContent = pad(h);
    cd.querySelector('[data-unit="m"]').textContent = pad(m);
    cd.querySelector('[data-unit="s"]').textContent = pad(s);
  }
  tick();
  setInterval(tick, 1000);
};

document.addEventListener('DOMContentLoaded', () => {
  ElectroMart.initFlashRail();
  ElectroMart.startCountdown();
});

})();
// Parallax background (subtle) — updates CSS vars --bgX/--bgY
(function () {
  const root = document.documentElement;
  const reduce = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (reduce) return;

  function onMove(e){
    const x = (e.clientX || window.innerWidth/2) / window.innerWidth;
    const y = (e.clientY || 200) / window.innerHeight;
    const bx = 40 + x*20;  // 40% → 60%
    const by = 30 + y*20;  // 30% → 50%
    root.style.setProperty('--bgX', bx.toFixed(2) + '%');
    root.style.setProperty('--bgY', by.toFixed(2) + '%');
  }
  window.addEventListener('mousemove', onMove, {passive:true});
})();

document.addEventListener('DOMContentLoaded', () => {
  const initRail = (rootSel) => {
    const root = document.querySelector(rootSel);
    if (!root) return;

    const track = root.querySelector('.dr-track');
    const prev  = root.querySelector('.dr-nav.prev');
    const next  = root.querySelector('.dr-nav.next');

    const step = () => Math.max(260 * 3, root.clientWidth * 0.9); // cuộn ~3 card hoặc 90% viewport

    const update = () => {
      const max = track.scrollWidth - track.clientWidth - 1;
      prev.disabled = track.scrollLeft <= 0;
      next.disabled = track.scrollLeft >= max;
    };

    prev.addEventListener('click', () => track.scrollBy({ left: -step(), behavior: 'smooth' }));
    next.addEventListener('click', () => track.scrollBy({ left:  step(), behavior: 'smooth' }));
    track.addEventListener('scroll', () => requestAnimationFrame(update));
    window.addEventListener('resize', update);
    update();
  };

  initRail('#hot-deals');
});
