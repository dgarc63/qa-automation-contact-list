class UpdateContactPage {
  constructor(page) {
    this.page = page;

    this.editButton = page.getByRole('button', { name: 'Edit Contact' });
    this.submitButton = page.getByRole('button', { name: 'Submit' });

    this.firstName = page.locator('#firstName');
    this.lastName = page.locator('#lastName');
    this.birthdate = page.locator('#birthdate');
    this.email = page.locator('#email');
    this.phone = page.locator('#phone');
    this.street1 = page.locator('#street1');
    this.street2 = page.locator('#street2');
    this.city = page.locator('#city');
    this.stateProvince = page.locator('#stateProvince');
    this.postalCode = page.locator('#postalCode');
    this.country = page.locator('#country');

    this.errorText = page.locator('#error');
  }

  async openContact(fullName) {
    await this.page.getByText(fullName, { exact: false }).click();
    await this.editButton.waitFor({ state: 'visible', timeout: 15000 });
  }

  async updateContactKeepAll(data) {
    await this.editButton.click();
    await this.firstName.waitFor({ state: 'visible', timeout: 15000 });

    await this.firstName.fill(data.firstName);
    await this.lastName.fill(data.lastName);
    await this.birthdate.fill(data.dob);
    await this.email.fill(data.email);
    await this.phone.fill(data.phone);
    await this.street1.fill(data.address1);
    await this.street2.fill(data.address2);
    await this.city.fill(data.city);
    await this.stateProvince.fill(data.state);
    await this.postalCode.fill(data.postal);
    await this.country.fill(data.country);

    // ✅ espera el PUT real antes de continuar
    await Promise.all([
      this.page.waitForResponse((res) =>
        res.url().includes('/contacts/') &&
        res.request().method() === 'PUT' &&
        res.status() === 200
      , { timeout: 15000 }),
      this.submitButton.click(),
    ]);

    // ✅ señal visual de que volvió al detalle
    await this.editButton.waitFor({ state: 'visible', timeout: 15000 });

    // si aparece error, lo lees; si no, no bloquea
    if (await this.errorText.count() > 0) {
      const msg = (await this.errorText.first().textContent())?.trim();
      if (msg) throw new Error(`Update falló con validación: ${msg}`);
    }

    // ✅ vuelve a lista, pero esperando carga estable
    await this.page.goto('https://thinking-tester-contact-list.herokuapp.com/contactList', {
      waitUntil: 'domcontentloaded',
    });

    await this.page.getByRole('button', { name: 'Add a New Contact' })
      .waitFor({ state: 'visible', timeout: 15000 });
  }
}

module.exports = { UpdateContactPage };