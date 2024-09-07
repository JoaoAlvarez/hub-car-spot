import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CombustivelDetailComponent } from './combustivel-detail.component';

describe('Combustivel Management Detail Component', () => {
  let comp: CombustivelDetailComponent;
  let fixture: ComponentFixture<CombustivelDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CombustivelDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./combustivel-detail.component').then(m => m.CombustivelDetailComponent),
              resolve: { combustivel: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CombustivelDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CombustivelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load combustivel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CombustivelDetailComponent);

      // THEN
      expect(instance.combustivel()).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
