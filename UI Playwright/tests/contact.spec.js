const { test, expect } = require('@playwright/test');
const { LoginPage } = require('../pages/login.page');
const { ContactPage } = require('../pages/contact.page');
const { UpdateContactPage } = require('../pages/update.contact.page');
const { DeleteContactPage } = require('../pages/delete.contact.page');

test('CRUD contacto: crear, actualizar, eliminar y cerrar sesión', async ({ page }) => {
  const login = new LoginPage(page);
  const contact = new ContactPage(page);
  const updater = new UpdateContactPage(page);
  const remover = new DeleteContactPage(page);

  const suffix = String(Date.now()).slice(-5);

  const contactData = {
    firstName: `Jose${suffix}`,
    lastName: 'Jimenez',
    dob: '2004-08-27',
    email: `jose${suffix}@mail.com`,
    phone: '3104404070',
    address1: 'avenida',
    address2: 'siempreviva',
    city: 'bogota',
    state: 'cundinamarca',
    postal: '110511',
    country: 'colombia'
  };

  await login.navigate();
  await login.login('dgarc62@gmail.com', '12345678');

  await expect(page.getByRole('button', { name: 'Add a New Contact' }))
    .toBeVisible({ timeout: 15000 });

  // ===== CREATE =====
  await contact.addFullContact(contactData);

  const createdFullName = `${contactData.firstName} ${contactData.lastName}`;
  await contact.waitForContact(createdFullName, 15000);

  // ===== UPDATE =====
  await updater.openContact(createdFullName);

  const updatedFirstName = `Gerardo${suffix}`;
  const updatedData = { ...contactData, firstName: updatedFirstName };

  await updater.updateContactKeepAll(updatedData);

  const updatedFullName = `${updatedFirstName} ${contactData.lastName}`;

  // ✅ lista fresca + espera robusta
  await page.goto('https://thinking-tester-contact-list.herokuapp.com/contactList', {
    waitUntil: 'domcontentloaded',
  });

  await contact.waitForContact(updatedFullName, 15000);

  // ===== DELETE =====
  await remover.openContact(updatedFullName);
  await remover.deleteContact();

  // ✅ lista fresca antes de validar ausencia
  await page.goto('https://thinking-tester-contact-list.herokuapp.com/contactList', {
    waitUntil: 'domcontentloaded',
  });

  await expect(contact.contactRow(updatedFullName))
    .toHaveCount(0, { timeout: 15000 });

  await contact.logout();
});