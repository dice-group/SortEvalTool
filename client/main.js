const URL_BASE = 'http://localhost:8080';
const appData = {
  category: '',
  uris: [],
  toMerge: [],
  results: [],
  itemLeft: {},
  itemRight: {},
  fromOneArr: false,
};

const split = arr => {
  const length = arr.length;

  if (length <= 2) {
    return appData.toMerge.push({
      data: arr,
      sorted: false,
    });
  }

  const middle = Math.floor(length / 2);
  const left = split(arr.slice(0, middle));
  const right = split(arr.slice(middle));

  return [left, right];
};

const abstractURI = 'http://dbpedia.org/ontology/abstract';
const nameURI = 'http://dbpedia.org/property/name';
const labelURI = 'http://www.w3.org/2000/01/rdf-schema#label';
const getValue = (obj, lang = 'en') =>
  obj.filter(it => it.lang === lang).pop().value;
const triplesToObject = (triples, uri) => {
  const result = {
    uri,
  };

  // get abstract
  if (triples[abstractURI]) {
    result.abstract = getValue(triples[abstractURI]);
  }

  // get name/label
  if (triples[labelURI]) {
    result.name = getValue(triples[labelURI]);
  } else if (triples[nameURI]) {
    result.name = getValue(triples[nameURI]);
  }

  return result;
};

const save = async () => {
  const {results} = appData;
  const body = new FormData();
  body.append(
    'json',
    JSON.stringify({
      results,
    })
  );
  // TODO: uncomment me when save endpoint is implemented
  // fetch(`${URL_BASE}/save`, {
  // method: 'POST',
  // body,
  // })
  // .then(() => {
    getData();
  // })
  // .catch(e => console.error("Couldn't save results:", e));
};

const nextPair = async () => {
  const [firstArr, secondArr] = appData.toMerge;

  if (!secondArr) {
    appData.itemLeft = {};
    appData.itemRight = {};
    appData.results = appData.toMerge.shift().data;
    save();
    return;
  }

  if (!firstArr.data.length && !secondArr.data.length) {
    appData.toMerge.splice(0, 2);
    appData.toMerge.push({
      data: appData.results,
      sorted: true,
    });
    appData.results = [];
    nextPair();
    return;
  }

  if (!firstArr.data.length) {
    appData.results.push(secondArr.data.shift());
    nextPair();
    return;
  }

  if (!secondArr.data.length) {
    appData.results.push(firstArr.data.shift());
    nextPair();
    return;
  }

  let leftOrigUri;
  let rightOrigUri;

  if (firstArr.data.length === 2 && !firstArr.sorted) {
    leftOrigUri = firstArr.data[0];
    rightOrigUri = firstArr.data[1];
    appData.fromOneArr = true;
  } else if (secondArr.data.length === 2 && !secondArr.sorted) {
    leftOrigUri = secondArr.data[0];
    rightOrigUri = secondArr.data[1];
    appData.fromOneArr = true;
  } else {
    // get uris
    leftOrigUri = firstArr.data[0];
    rightOrigUri = secondArr.data[0];
    appData.fromOneArr = false;
  }

  // fetch data from dbpedia
  const leftUri = leftOrigUri.replace('/resource/', '/data/') + '.json';
  const rightUri = rightOrigUri.replace('/resource/', '/data/') + '.json';
  const left = await fetch(leftUri).then(r => r.json());
  const right = await fetch(rightUri).then(r => r.json());

  // transform
  const leftData = triplesToObject(left[leftOrigUri], leftOrigUri);
  const rightData = triplesToObject(right[rightOrigUri], rightOrigUri);

  // assign
  appData.itemLeft = leftData;
  appData.itemRight = rightData;
};

const pick = item => {
  const [firstArr, secondArr] = appData.toMerge;

  if (appData.fromOneArr) {
    if (appData.itemRight.uri === item.uri) {
      if (!firstArr.sorted && firstArr.data.length === 2) {
        const [one, two] = firstArr.data;
        appData.toMerge[0] = {
          data: [two, one],
          sorted: true,
        };
      } else if (!secondArr.sorted && secondArr.data.length === 2) {
        const [one, two] = secondArr.data;
        appData.toMerge[1] = {
          data: [two, one],
          sorted: true,
        };
      }
    } else {
      if (!firstArr.sorted && firstArr.data.length === 2) {
        appData.toMerge[0].sorted = true;
      } else if (!secondArr.sorted && secondArr.data.length === 2) {
        appData.toMerge[1].sorted = true;
      }
    }
    nextPair();
    return;
  }

  if (appData.itemLeft.uri === item.uri) {
    appData.results.push(firstArr.data.shift());
  } else if (appData.itemRight.uri === item.uri) {
    appData.results.push(secondArr.data.shift());
  }

  nextPair();
};

const getData = async () => {
  const json = await fetch(`${URL_BASE}/next`).then(r => r.json());
  appData.toMerge = [];
  appData.results = [];
  appData.itemLeft = {};
  appData.itemRight = {};
  appData.fromOneArr = false;
  appData.category = json.category;
  appData.uris = json.uri;
  // split into arrays for merge-sort
  split(appData.uris);
  // get next pair for comparison
  nextPair();
};

const app = new Vue({
  el: '#app',
  data: appData,
  created: getData,
  methods: {
    pick,
  },
});
