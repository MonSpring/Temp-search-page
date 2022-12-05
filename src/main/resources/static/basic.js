
// infinite scroll

const URL = "/infinitescroll"
let lastId = 0;
let isFetching = false;
const TABLE_ELE = document.querySelector(".getAllSearch");

const drawList = (DATA) => {
    let listHtml = "";
    DATA.forEach((item) => {
        const { book_id, title, author, publisher, book_count, isbn } = item;
        const TR_ELE = document.createElement('tr');
        listHtml = `<td>${book_id}</td>
        <td>${title}</td>
        <td>${author}</td>
        <td>${publisher}</td>
        <td>${book_count}</td>
        <td>${isbn}</td>`;

        // if (index === DATA.length - 1) {
        //     lastId += 30;
        //     console.log("현재 라스트 아이디 : " + lastId);
        // } // 마지막건 ID 저장

        TR_ELE.innerHTML = listHtml;
        TABLE_ELE.appendChild(TR_ELE);
    });

    isFetching = false; // callback 끝났으니 isFetching 리셋

};

const getList = () => {
    isFetching = true; // 아직 callback 끝나지 않았어요!
    fetch(URL, {
        method: "POST",
        body: {
            lastId
        }
    })
        .then((res) => res.json())
        .then((resJson) => {
            drawList(resJson);
        });
};

window.addEventListener("scroll", function () {
    const SCROLLED_HEIGHT = window.scrollY;
    const WINDOW_HEIGHT = window.innerHeight;
    const DOC_TOTAL_HEIGHT = document.body.offsetHeight;
    const IS_BOTTOM = WINDOW_HEIGHT + SCROLLED_HEIGHT === DOC_TOTAL_HEIGHT; // 완전히 닿으면 로드
    const IS_END = (WINDOW_HEIGHT + SCROLLED_HEIGHT > DOC_TOTAL_HEIGHT - 500); // 완전히 닿기 전 로드

    if ((IS_END && !isFetching) || (IS_BOTTOM)) {
        getList();
    }
});
