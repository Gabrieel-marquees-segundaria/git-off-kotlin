
/**
 * 
 * @param {*} dict 
 * @param {*} selectId 
 * @param {*} callback 
 * @returns 
 * 
 * @example
 * 
 * function callback(event) {
 *  const value = event.target.value;
 * ... code ...
 * }
 */
export function dictKeysToSelect(dict, selectId = "mySelect", callback= (event)=> {}) {
  const select = document.createElement("select");
  select.id = selectId;

  Object.keys(dict).forEach(key => {
    const option = document.createElement("option");
    option.value = key;
    option.textContent = key;
    select.appendChild(option);
  });

  select.addEventListener("change", callback)

  return select;
}