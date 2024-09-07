import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICompraVeiculo } from '../compra-veiculo.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../compra-veiculo.test-samples';

import { CompraVeiculoService, RestCompraVeiculo } from './compra-veiculo.service';

const requireRestSample: RestCompraVeiculo = {
  ...sampleWithRequiredData,
  dataCompra: sampleWithRequiredData.dataCompra?.format(DATE_FORMAT),
};

describe('CompraVeiculo Service', () => {
  let service: CompraVeiculoService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompraVeiculo | ICompraVeiculo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CompraVeiculoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CompraVeiculo', () => {
      const compraVeiculo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compraVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompraVeiculo', () => {
      const compraVeiculo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compraVeiculo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompraVeiculo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompraVeiculo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompraVeiculo', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompraVeiculoToCollectionIfMissing', () => {
      it('should add a CompraVeiculo to an empty array', () => {
        const compraVeiculo: ICompraVeiculo = sampleWithRequiredData;
        expectedResult = service.addCompraVeiculoToCollectionIfMissing([], compraVeiculo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compraVeiculo);
      });

      it('should not add a CompraVeiculo to an array that contains it', () => {
        const compraVeiculo: ICompraVeiculo = sampleWithRequiredData;
        const compraVeiculoCollection: ICompraVeiculo[] = [
          {
            ...compraVeiculo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompraVeiculoToCollectionIfMissing(compraVeiculoCollection, compraVeiculo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompraVeiculo to an array that doesn't contain it", () => {
        const compraVeiculo: ICompraVeiculo = sampleWithRequiredData;
        const compraVeiculoCollection: ICompraVeiculo[] = [sampleWithPartialData];
        expectedResult = service.addCompraVeiculoToCollectionIfMissing(compraVeiculoCollection, compraVeiculo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compraVeiculo);
      });

      it('should add only unique CompraVeiculo to an array', () => {
        const compraVeiculoArray: ICompraVeiculo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compraVeiculoCollection: ICompraVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addCompraVeiculoToCollectionIfMissing(compraVeiculoCollection, ...compraVeiculoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compraVeiculo: ICompraVeiculo = sampleWithRequiredData;
        const compraVeiculo2: ICompraVeiculo = sampleWithPartialData;
        expectedResult = service.addCompraVeiculoToCollectionIfMissing([], compraVeiculo, compraVeiculo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compraVeiculo);
        expect(expectedResult).toContain(compraVeiculo2);
      });

      it('should accept null and undefined values', () => {
        const compraVeiculo: ICompraVeiculo = sampleWithRequiredData;
        expectedResult = service.addCompraVeiculoToCollectionIfMissing([], null, compraVeiculo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compraVeiculo);
      });

      it('should return initial array if no CompraVeiculo is added', () => {
        const compraVeiculoCollection: ICompraVeiculo[] = [sampleWithRequiredData];
        expectedResult = service.addCompraVeiculoToCollectionIfMissing(compraVeiculoCollection, undefined, null);
        expect(expectedResult).toEqual(compraVeiculoCollection);
      });
    });

    describe('compareCompraVeiculo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompraVeiculo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareCompraVeiculo(entity1, entity2);
        const compareResult2 = service.compareCompraVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareCompraVeiculo(entity1, entity2);
        const compareResult2 = service.compareCompraVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareCompraVeiculo(entity1, entity2);
        const compareResult2 = service.compareCompraVeiculo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
