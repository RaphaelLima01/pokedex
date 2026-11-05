const cores ={
   fire: '#f97316', water: '#60a5fa', grass: '#4ade80', electric: '#facc15',
      psychic: '#f472b6', ice: '#67e8f9', dragon: '#818cf8', dark: '#9ca3af',
      fairy: '#fbcfe8', fighting: '#ef4444', poison: '#c084fc', ground: '#d6a35c',
      rock: '#a8a29e', bug: '#a3e635', ghost: '#a78bfa', steel: '#cbd5e1',
      flying: '#93c5fd', normal: '#e5e5e5'
};

const nomeStatus={
    'hp': 'Vida(HP)', 'attack': 'Ataque', 'defense':'Defesa',
    'special-attack': 'Ataque Especial', 'special-defense':'Defesa Especial',
    'speed': 'Velocidade'
};

const campoNome = document.getElementById('campoNome');
const btn = document.getElementById("botton");
const msg = document.getElementById("mensagem");
const card = document.getElementById("card");

async function buscar() {
    const nome = campoNome.value.trim();
    msg.textContent ="";

    if(nome === ""){
        msg.textContent="Escreva o nome de um Pokémon";
        return;
    }
    try{
        const resposta = await fetch('/api/pokemon?nome=' + encodeURIComponent(nome));
        const dados = await resposta.json();

        if(!resposta.ok){
           msg.textContent=dados.erro;
            return;
        }
        mostrar(dados);
    }catch(erro){
        console.error(erro);
        msg.textContent= "Algo deu errado";
    }
}

function mostrar(p){
    document.getElementById("nomeP").textContent = p.name;
    document.getElementById("idP").textContent = '#'+p.id;
    document.getElementById("medidas").textContent =
    'Altura:' + (p.height/10) + 'm | Peso:' + (p.weight/10) + 'kg';;
    document.getElementById("img").src =
    p.sprites.other?.['official-artwork']?.front_default
    || p.sprites.front_default;
        'Altura:'+(p.height/10) + 'm | Peso:'+(p.weight/10)+'kg';


/// TIPOS:

const tipos = document.getElementById("tipos");
tipos.innerHTML=''; //esvazia oque havia antes
p.types.forEach(t=>{
    const etiqueta = document.createElement('span');
    etiqueta.className = 'tipo';
    etiqueta.textContent=t.type.name;
    etiqueta.style.background = cores[t.type.name] || '#e5e5e5';
    tipos.appendChild(etiqueta);
});

//STATUS:
const stats = document.getElementById('status');
stats.innerHTML='';
p.stats.forEach(s => {
    const largura = Math.min(100, Math.round(s.base_stat *100/255));
    const linha = document.createElement('div');
    linha.className = 'linha';
    linha.innerHTML =  
    '<span class="nome">' + (nomeStatus[s.stat.name] || s.stat.name) + '</span>' +
    '<div class="fundo"><div class="barra" style="width:' + largura + '%"></div></div>' +
    '<span class="valor">' + s.base_stat + '</span>';
    
   stats.appendChild(linha);
});

//GOLPES:
//slice(0,4) => pede os 4 primeiro golpes do pkmn
const golpes = document.getElementById("golpes");
golpes.innerHTML='';
p.moves.slice(0,4).forEach(m =>{
const item = document.createElement("li");
item.textContent = m.move.name.replaceAll('-', ' ');
golpes.appendChild(item);

});

card.classList.remove('escondido');

}

btn.addEventListener("click", buscar);

campoNome.addEventListener('keydown', e =>{
    if(e.key === 'Enter') buscar();
})