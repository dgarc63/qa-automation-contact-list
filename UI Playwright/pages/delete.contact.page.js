class DeleteContactPage {
  constructor(page) {
    this.page = page;
    this.deleteButton = page.getByRole('button', { name: 'Delete Contact' });
    this.addNewContactButton = page.getByRole('button', { name: 'Add a New Contact' });
  }

  async openContact(fullName) {
    await this.page.getByText(fullName, { exact: false }).click();
    await this.deleteButton.waitFor({ state: 'visible', timeout: 15000 });
    await this.deleteButton.scrollIntoViewIfNeeded();
  }

  async deleteContact() {
    await this.deleteButton.waitFor({ state: 'visible', timeout: 15000 });

    // ✅ captura SOLO el próximo dialog y lo acepta
    this.page.once('dialog', async (dialog) => {
      await dialog.accept();
    });

    await this.deleteButton.click();

    // señal de regreso a lista
    await this.addNewContactButton.waitFor({ state: 'visible', timeout: 15000 });
  }
}

module.exports = { DeleteContactPage };