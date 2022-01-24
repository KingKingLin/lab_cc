import xlsx from 'xlsx';

export default {
    data() {
        return {
            // 字段对应表
            characters: {
                id: {
                    text: "学号",
                    type: "string"
                },
                name: {
                    text: "姓名",
                    type: "string",
                }
            }
        }
    },
    method: {
        // dom读取文件
        readFile(file) {
            // 异步操作
            return new Promise(resolve => {
                const reader = new FileReader();
                reader.readAsBinaryString(file); // 以二进制形式读取文件，解析内存中的file
                // 加载完成
                reader.onload = ev => {
                    // @ts-ignore
                    resolve(ev.target.result); // 把文件的二进制形式返回
                }
            })
        },
        // 解析xlsx数据
        analyse(data) {
            const workBook = xlsx.read(data, { type: "binary"});
            // 默认拿第一张表
            const sheet = workBook.Sheets[workBook.SheetNames[0]];
            // console.log("sheet: {}", sheet);
            data = xlsx.utils.sheet_to_json(sheet);
            return data;
        },
        changeCharacters(characters, data) {
            const arr = [];
            data.forEach((item) => {
                let obj = {};
                for (let key in characters) {
                    // 如果是私有属性则退出
                    // if (!XLSXTools.characters.hasOwnProperty(key)) break; //Do not access Object.prototype method 'hasOwnProperty' from target object  no-prototype-builtins
                    let v = characters[key],
                        text = v.text,
                        type = v.type;
                    v = item[text] || "";
                    type === "date"? ((v = Date(v))) : null;
                    type === "string"? ((v = String(v))) : null;
                    obj[key] = v;
                }
                arr.push(obj);
            });
            return arr;
        }
    }
}