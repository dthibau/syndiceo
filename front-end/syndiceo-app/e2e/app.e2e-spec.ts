import { SyndiceoAppPage } from './app.po';

describe('syndiceo-app App', function() {
  let page: SyndiceoAppPage;

  beforeEach(() => {
    page = new SyndiceoAppPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
