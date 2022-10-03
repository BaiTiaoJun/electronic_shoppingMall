window.addEventListener('click', ()=>{
    const $ = (s,all) => document[all ? 'querySelectorAll' : 'querySelector'](s);

    $('.n-inputNumber',1).forEach(el=>{
        const [input, l, r] = [el.querySelector('.n-inputNumber-input'),el.querySelector('.n-inputNumber-l'),el.querySelector('.n-inputNumber-r') ];

        if(!input) return;
        let [min,max] = [input.getAttribute('min'),input.getAttribute('max')  ];

        min = min === null ||  !min && min * 1 !== 0 ? Number.MIN_SAFE_INTEGER : min * 1;
        max = max === null ||  !max && max * 1 !== 0  ? Number.MAX_SAFE_INTEGER : max * 1;

        input.onchange = e=> e.value = e.value * 1 || 0;

        const onChange = n=>{
            let value = input.value * 1 || 0;
            value+=n;
            console.log(value , min ,max)
            value = value < min ? min : value > max ? max : value;
            input.value = value;
        }

        if(l) l.onclick = ()=> onChange(-1);
        if(r) r.onclick = ()=> onChange(1);

    })
    let time = 5000
    setInterval(function (){
        document.getElementsByClassName("swiper-arrow next next-0")[0].click()
    },time)
})