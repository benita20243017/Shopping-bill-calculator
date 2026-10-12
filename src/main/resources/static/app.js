const API_URL = '/api/items';
const BILL_URL = '/api/bill';

const itemForm = document.getElementById('item-form');
const resetFormButton = document.getElementById('reset-form');
const itemsTableBody = document.getElementById('items-table-body');
const calculateBillButton = document.getElementById('calculate-bill');

const fields = {
  id: document.getElementById('item-id'),
  name: document.getElementById('name'),
  category: document.getElementById('category'),
  quantity: document.getElementById('quantity'),
  price: document.getElementById('price'),
  description: document.getElementById('description')
};

function formatCurrency(value) {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(value || 0);
}

async function fetchItems() {
  const response = await fetch(API_URL);
  const items = await response.json();
  renderItems(items);
  if (items.length) {
    calculateBill();
  } else {
    clearSummary();
  }
}

function renderItems(items) {
  itemsTableBody.innerHTML = '';

  items.forEach(item => {
    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${item.name}</td>
      <td>${item.category}</td>
      <td>${item.quantity}</td>
      <td>${formatCurrency(item.price)}</td>
      <td>
        <button class="action-btn edit-btn" data-id="${item.id}">Edit</button>
        <button class="action-btn delete-btn" data-id="${item.id}">Delete</button>
      </td>
    `;
    itemsTableBody.appendChild(row);
  });

  itemsTableBody.querySelectorAll('.edit-btn').forEach(button => {
    button.addEventListener('click', async () => {
      const id = button.getAttribute('data-id');
      const item = await fetch(`${API_URL}/${id}`).then(res => res.json());
      fields.id.value = item.id;
      fields.name.value = item.name;
      fields.category.value = item.category;
      fields.quantity.value = item.quantity;
      fields.price.value = item.price;
      fields.description.value = item.description || '';
    });
  });

  itemsTableBody.querySelectorAll('.delete-btn').forEach(button => {
    button.addEventListener('click', async () => {
      const id = button.getAttribute('data-id');
      await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
      await fetchItems();
    });
  });
}

async function saveItem(event) {
  event.preventDefault();

  const payload = {
    name: fields.name.value,
    category: fields.category.value,
    quantity: Number(fields.quantity.value),
    price: Number(fields.price.value),
    description: fields.description.value
  };

  const method = fields.id.value ? 'PUT' : 'POST';
  const url = fields.id.value ? `${API_URL}/${fields.id.value}` : API_URL;

  await fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });

  clearForm();
  await fetchItems();
}

function clearForm() {
  itemForm.reset();
  fields.id.value = '';
  fields.quantity.value = 1;
}

async function calculateBill() {
  const taxRate = Number(document.getElementById('tax-rate').value || 0) / 100;
  const discountRate = Number(document.getElementById('discount-rate').value || 0) / 100;

  const response = await fetch(`${BILL_URL}?taxRate=${taxRate}&discountRate=${discountRate}`);
  const summary = await response.json();

  document.getElementById('subtotal').textContent = formatCurrency(summary.subtotal);
  document.getElementById('tax').textContent = formatCurrency(summary.taxAmount);
  document.getElementById('discount').textContent = formatCurrency(summary.discountAmount);
  document.getElementById('total').textContent = formatCurrency(summary.total);
}

function clearSummary() {
  document.getElementById('subtotal').textContent = formatCurrency(0);
  document.getElementById('tax').textContent = formatCurrency(0);
  document.getElementById('discount').textContent = formatCurrency(0);
  document.getElementById('total').textContent = formatCurrency(0);
}

itemForm.addEventListener('submit', saveItem);
resetFormButton.addEventListener('click', () => {
  clearForm();
});
calculateBillButton.addEventListener('click', calculateBill);

fetchItems();
