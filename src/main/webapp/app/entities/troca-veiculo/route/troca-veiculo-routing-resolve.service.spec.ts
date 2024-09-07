import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ITrocaVeiculo } from '../troca-veiculo.model';
import { TrocaVeiculoService } from '../service/troca-veiculo.service';

import trocaVeiculoResolve from './troca-veiculo-routing-resolve.service';

describe('TrocaVeiculo routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: TrocaVeiculoService;
  let resultTrocaVeiculo: ITrocaVeiculo | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(TrocaVeiculoService);
    resultTrocaVeiculo = undefined;
  });

  describe('resolve', () => {
    it('should return ITrocaVeiculo returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        trocaVeiculoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTrocaVeiculo = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('ABC');
      expect(resultTrocaVeiculo).toEqual({ id: 'ABC' });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        trocaVeiculoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTrocaVeiculo = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTrocaVeiculo).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITrocaVeiculo>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 'ABC' };

      // WHEN
      TestBed.runInInjectionContext(() => {
        trocaVeiculoResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTrocaVeiculo = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith('ABC');
      expect(resultTrocaVeiculo).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
