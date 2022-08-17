/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.unscramble.ui.game


const val MAX_NO_OF_WORDS = 10
const val SCORE_INCREASE = 20

// List with all the words for the Game
val allWordsListEng: List<String> =
    listOf(
        "animal",
        "auto",
        "all",
        "awesome",
        "arise",
        "basket",
        "bench",
        "best",
        "book",
        "camera",
        "camping",
        "candle",
        "cat",
        "chat",
        "class",
        "classic",
        "coffee",
        "cookie",
        "cruise",
        "dance",
        "daytime",
        "dine",
        "dream",
        "dusk",
        "eating",
        "emerald",
        "eerie",
        "finish",
        "flowers",
        "follow",
        "fox",
        "frame",
        "free",
        "funnel",
        "green",
        "guitar",
        "grocery",
        "glass",
        "great",
        "giggle",
        "haircut",
        "half",
        "happen",
        "honey",
        "hurry",
        "hundred",
        "ice",
        "igloo",
        "invest",
        "invite",
        "icon",
        "joke",
        "jovial",
        "journal",
        "jump",
        "join",
        "koala",
        "kind",
        "late",
        "laugh",
        "lemon",
        "letter",
        "lily",
        "marine",
        "maze",
        "melody",
        "minute",
        "moon",
        "music",
        "north",
        "nose",
        "night",
        "name",
        "never",
        "number",
        "octopus",
        "oak",
        "order",
        "open",
        "polar",
        "pack",
        "person",
        "picnic",
        "pillow",
        "pizza",
        "podcast",
        "puppy",
        "puzzle",
        "recipe",
        "release",
        "revolve",
        "rewind",
        "room",
        "run",
        "secret",
        "seed",
        "ship",
        "shirt",
        "should",
        "small",
        "skill",
        "street",
        "style",
        "sunrise",
        "taxi",
        "tidy",
        "timer",
        "tooth",
        "tourist",
        "travel",
        "truck",
        "under",
        "useful",
        "unicorn",
        "unique",
        "uplift",
        "uniform",
        "vase",
        "violin",
        "visitor",
        "vision",
        "volume",
        "view",
        "walrus",
        "wander",
        "world",
        "winter",
        "well",
        "x-ray",
        "yoga",
        "yogurt",
        "yoyo",
        "you",
        "year",
        "yummy",
        "zebra",
        "zigzag",
        "zoology",
        "zone",
        "zeal"
    )

val allWordsListRu: List<String> = listOf(
    "атлас",
    "слово",
    "цифры",
    "набор",
    "аркан",
    "кости",
    "форма",
    "масса",
    "спуск",
    "закон",
    "орган",
    "гарем",
    "барон",
    "семья",
    "заумь",
    "абзац",
    "валек",
    "опция",
    "загон",
    "вывод",
    "сорт",
    "бакс",
    "ввод",
    "йога",
    "лего",
    "база",
    "свод",
    "крой",
    "чушь",
    "лафа",
    "баба",
    "дядя",
    "шкет",
    "авто",
    "фарт",
    "след",
    "жлоб",
    "псих",
    "теха",
    "атом",
    "гроб",
    "тело",
    "пара",
    "батя",
    "рало",
    "харя",
    "олух",
    "кляп",
    "горн",
    "смак",
    "репа",
    "алик",
    "влас",
    "тётя",
    "кино",
    "нога",
    "беда",
    "факт",
    "сила",
    "эфир",
    "рука",
    "босс",
    "жила",
    "стол",
    "ряха",
    "обок",
    "ввоз",
    "вата",
    "брёх",
    "линч",
    "опак",
    "шмат",
    "фриц",
    "таль",
    "форс",
    "вещь",
    "сырь",
    "рычаг",
    "малец",
    "харчи",
    "краля",
    "демос",
    "узина",
    "ворот",
    "амебы",
    "мошна",
    "леший",
    "мотив",
    "фофан",
    "барыш",
    "трояк",
    "кагал",
    "руффо",
    "туфта",
    "ехида",
    "калым",
    "строп",
    "выезд",
    "метан",
    "шавка",
    "космы",
    "стынь",
    "кефир",
    "отвес",
    "годок",
    "арден",
    "долги",
    "разор",
    "хайло",
    "мужик",
    "холка",
    "макар",
    "спора",
    "шкура",
    "ложка",
    "бабич",
    "турок",
    "бланш",
    "зевок",
    "сутки",
    "синтез",
    "металл",
    "курево",
    "кубизм",
    "хребет",
    "хозяин",
    "собака",
    "егоров",
    "выжига",
    "нозема",
    "халява",
    "игарка",
    "зохраб",
    "шмотки",
    "писака",
    "жгутик",
    "шматок",
    "канапе",
    "коралл",
    "портач",
    "трубач",
    "гаврик",
    "аммиак",
    "кухарь",
    "алхимия",
    "деление",
    "литорея",
    "трудяга",
    "стентор",
    "табакур",
    "бабезия",
    "лучевик",
    "история",
    "запарка",
    "манатки",
    "ульянов",
    "протеин",
    "хвороба",
    "сопатка",
    "эймерия",
    "хилодон",
    "порядок",
    "запруда",
    "мужчина"
)