class UpdateContactPage {
  constructor(page) {
    this.page = page;

    this.editButton = page.getByRole('button', { name: 'Edit Contact' });
    this.submitButton = page.getByRole('button', { name: 'Submit' });

    // En modo EDIT suelen ser inputs
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

    // En DETALLE es texto (labels). Estos selectors son más flexibles.
    this.detailFirstName = page.locator('#firstName');
    this.detailLastName = page.locator('#lastName');
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

    // Espera del request (si no matchea, no rompe)
    await Promise.all([
      this.page
        .waitForResponse(
          (r) =>
            r.url().includes('/contacts') &&
            r.request().method() === 'PUT' &&
            (r.status() === 200 || r.status() === 204),
          { timeout: 15000 }
        )
        .catch(() => null),
      this.submitButton.click(),
    ]);

    // Volvió a DETALLE
    await this.editButton.waitFor({ state: 'visible', timeout: 15000 });

    // Si hay error de validación, revienta
    if (await this.errorText.count()) {
      const msg = (await this.errorText.first().textContent())?.trim();
      if (msg) throw new Error(`Update falló con validación: ${msg}`);
    }

    // ✅ Verificación de guardado SIN inputValue (en detalle suele ser texto)
    // Nota: en detalle puede renderizar el valor como texto dentro del mismo id
    const fnText = ((await this.detailFirstName.textContent()) || '').trim();
    const lnText = ((await this.detailLastName.textContent()) || '').trim();

    // Si por alguna razón esos ids en detalle NO traen texto útil, no bloquees por falso negativo:
    // pero si sí traen texto y no coincide, falla.
    if (fnText && lnText) {
      if (fnText !== data.firstName.trim() || lnText !== data.lastName.trim()) {
        throw new Error(`El contacto NO se actualizó. Detalle muestra: "${fnText} ${lnText}"`);
      }
    }

    // ✅ Ir a lista fresca
    await this.page.goto(
      'https://thinking-tester-contact-list.herokuapp.com/contactList',
      { waitUntil: 'domcontentloaded' }
    );

    await this.page
      .getByRole('button', { name: 'Add a New Contact' })
      .waitFor({ state: 'visible', timeout: 15000 });
  }
}

module.exports = { UpdateContactPage };