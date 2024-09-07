import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IStatusDocumento } from '../status-documento.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../status-documento.test-samples';

import { StatusDocumentoService } from './status-documento.service';

const requireRestSample: IStatusDocumento = {
  ...sampleWithRequiredData,
};

describe('StatusDocumento Service', () => {
  let service: StatusDocumentoService;
  let httpMock: HttpTestingController;
  let expectedResult: IStatusDocumento | IStatusDocumento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(StatusDocumentoService);
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

    it('should create a StatusDocumento', () => {
      const statusDocumento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(statusDocumento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StatusDocumento', () => {
      const statusDocumento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(statusDocumento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StatusDocumento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StatusDocumento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StatusDocumento', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStatusDocumentoToCollectionIfMissing', () => {
      it('should add a StatusDocumento to an empty array', () => {
        const statusDocumento: IStatusDocumento = sampleWithRequiredData;
        expectedResult = service.addStatusDocumentoToCollectionIfMissing([], statusDocumento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(statusDocumento);
      });

      it('should not add a StatusDocumento to an array that contains it', () => {
        const statusDocumento: IStatusDocumento = sampleWithRequiredData;
        const statusDocumentoCollection: IStatusDocumento[] = [
          {
            ...statusDocumento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStatusDocumentoToCollectionIfMissing(statusDocumentoCollection, statusDocumento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StatusDocumento to an array that doesn't contain it", () => {
        const statusDocumento: IStatusDocumento = sampleWithRequiredData;
        const statusDocumentoCollection: IStatusDocumento[] = [sampleWithPartialData];
        expectedResult = service.addStatusDocumentoToCollectionIfMissing(statusDocumentoCollection, statusDocumento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statusDocumento);
      });

      it('should add only unique StatusDocumento to an array', () => {
        const statusDocumentoArray: IStatusDocumento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const statusDocumentoCollection: IStatusDocumento[] = [sampleWithRequiredData];
        expectedResult = service.addStatusDocumentoToCollectionIfMissing(statusDocumentoCollection, ...statusDocumentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const statusDocumento: IStatusDocumento = sampleWithRequiredData;
        const statusDocumento2: IStatusDocumento = sampleWithPartialData;
        expectedResult = service.addStatusDocumentoToCollectionIfMissing([], statusDocumento, statusDocumento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(statusDocumento);
        expect(expectedResult).toContain(statusDocumento2);
      });

      it('should accept null and undefined values', () => {
        const statusDocumento: IStatusDocumento = sampleWithRequiredData;
        expectedResult = service.addStatusDocumentoToCollectionIfMissing([], null, statusDocumento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(statusDocumento);
      });

      it('should return initial array if no StatusDocumento is added', () => {
        const statusDocumentoCollection: IStatusDocumento[] = [sampleWithRequiredData];
        expectedResult = service.addStatusDocumentoToCollectionIfMissing(statusDocumentoCollection, undefined, null);
        expect(expectedResult).toEqual(statusDocumentoCollection);
      });
    });

    describe('compareStatusDocumento', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStatusDocumento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareStatusDocumento(entity1, entity2);
        const compareResult2 = service.compareStatusDocumento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareStatusDocumento(entity1, entity2);
        const compareResult2 = service.compareStatusDocumento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareStatusDocumento(entity1, entity2);
        const compareResult2 = service.compareStatusDocumento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
