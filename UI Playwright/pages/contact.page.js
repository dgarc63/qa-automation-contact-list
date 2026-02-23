class ContactPage {
  constructor(page) {
    this.page = page;

    this.addButton = page.getByRole('button', { name: 'Add a New Contact' });
    this.submitButton = page.getByRole('button', { name: 'Submit' });

    this.firstName = page.getByRole('textbox', { name: '* First Name:' });
    this.lastName = page.getByRole('textbox', { name: '* Last Name:' });
    this.dob = page.getByRole('textbox', { name: 'Date of Birth:' });
    this.email = page.getByRole('textbox', { name: 'Email:' });
    this.phone = page.getByRole('textbox', { name: 'Phone:' });
    this.address1 = page.getByRole('textbox', { name: 'Street Address 1:' });
    this.address2 = page.getByRole('textbox', { name: 'Street Address 2:' });
    this.city = page.getByRole('textbox', { name: 'City:' });
    this.state = page.getByRole('textbox', { name: 'State or Province:' });
    this.postal = page.getByRole('textbox', { name: 'Postal Code:' });
    this.country = page.getByRole('textbox', { name: 'Country:' });

    this.logoutButton = page.getByRole('button', { name: 'Logout' });
  }

  async addFullContact(data) {
    await this.addButton.click();

    await this.firstName.fill(data.firstName);
    await this.lastName.fill(data.lastName);
    await this.dob.fill(data.dob);
    await this.email.fill(data.email);
    await this.phone.fill(data.phone);
    await this.address1.fill(data.address1);
    await this.address2.fill(data.address2);
    await this.city.fill(data.city);
    await this.state.fill(data.state);
    await this.postal.fill(data.postal);
    await this.country.fill(data.country);

    await this.submitButton.click();
  }
// Abre un contacto por email (único)
async openContactByEmail(email) {
  // busca la fila que contiene ese email y hace click
  const row = this.page.locator('tr').filter({ hasText: email }).first();
  await row.waitFor({ state: 'visible', timeout: 15000 });
  await row.click();
}
  contactRow(fullName) {
    return this.page.getByText(fullName, { exact: false });
  }

  async logout() {
    await this.logoutButton.click();
  }
}

module.exports = { ContactPage };